/*
 *  Copyright 2024 LY Corporation
 *
 *  LY Corporation licenses this file to you under the Apache License,
 *  version 2.0 (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at:
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */
package com.linecorp.cse.scavenger.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.JBColor
import com.linecorp.cse.scavenger.utils.concatenateStrings
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode

@Service(Service.Level.PROJECT)
class KotlinMethodHighlighter : MethodHighlighter {

    companion object {
        private const val KOTLIN_EXTENSION = ".kt"
        private const val SUSPEND_FUNCTION_PARAM = "Continuation"
        private const val USAGE_COLOR = 0x294436
        private const val UNUSED_COLOR = 0x45302B
    }

    override fun highlightMethods(psiFile: PsiFile, snapshotNodeMap: Map<String, Boolean>, project: Project) {
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        val markupModel = editor.markupModel

        val ktClassName = (psiFile as KtFile).name.replace(KOTLIN_EXTENSION, "")
        val methods = PsiTreeUtil.findChildrenOfType(psiFile, KtNamedFunction::class.java)

        val packageName = (psiFile as? KtFile)?.packageFqName?.asString() ?: return

        val prefix = "$packageName.$ktClassName"

        for (method in methods) {
            val methodName = method.name
            val receiverTypeName = extractReceiverTypeName(method) ?: ""
            val paramTypeName = extractParameterTypeNames(method)

            // 추가 파라미터 확인 (suspend 함수의 경우)
            val isSuspend = method.hasModifier(org.jetbrains.kotlin.lexer.KtTokens.SUSPEND_KEYWORD)
            val extraParams = if (isSuspend) {
                SUSPEND_FUNCTION_PARAM
            } else ""

            val params = concatenateStrings(receiverTypeName, *paramTypeName.toTypedArray(), extraParams)

            val fullSignature = "$prefix.$methodName($params)"
            val isUsage: Boolean? = snapshotNodeMap[fullSignature]

            val color = if (isUsage ?: continue) JBColor(USAGE_COLOR, USAGE_COLOR) else JBColor(UNUSED_COLOR, UNUSED_COLOR)
            val attributes = TextAttributes().apply { backgroundColor = color }

            val startOffset = method.textRange.startOffset
            val endOffset = method.textRange.endOffset
            markupModel.addRangeHighlighter(startOffset, endOffset, 0, attributes, HighlighterTargetArea.EXACT_RANGE)
        }
    }

    private fun extractReceiverTypeName(function: KtNamedFunction): String? {
        val receiverTypeRef: KtTypeReference? = function.receiverTypeReference
        if (receiverTypeRef != null) {
            val bindingContext: BindingContext = function.analyze(BodyResolveMode.PARTIAL)
            val receiverType = bindingContext[BindingContext.TYPE, receiverTypeRef]
            if (receiverType != null) {
                return receiverType.toString()
            }
        }
        return null
    }

    private fun extractParameterTypeNames(function: KtNamedFunction): List<String> {
        val parameterTypes = mutableListOf<String>()
        val bindingContext: BindingContext = function.analyze(BodyResolveMode.PARTIAL)

        for (parameter in function.valueParameters) {
            val parameterTypeRef = parameter.typeReference
            if (parameterTypeRef != null) {
                val parameterType = bindingContext[BindingContext.TYPE, parameterTypeRef]
                if (parameterType != null) {
                    val typeName = parameterType.constructor.declarationDescriptor?.name?.asString() ?: ""
                    parameterTypes.add(typeName)
                }
            }
        }

        return parameterTypes
    }
}