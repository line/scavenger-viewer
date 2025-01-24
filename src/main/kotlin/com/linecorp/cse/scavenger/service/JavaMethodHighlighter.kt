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
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.JBColor
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.HIGHTLIGHER_LAYER
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log

@Service(Service.Level.PROJECT)
class JavaMethodHighlighter : MethodHighlighter {
    companion object {
        private const val JAVA_EXTENSION = ".java"
        private const val USAGE_COLOR = 0x294436
        private const val UNUSED_COLOR = 0x45302B
    }

    override fun highlightMethods(psiFile: PsiFile, snapshotNodeMap: Map<String, Boolean>, project: Project) {
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        val markupModel = editor.markupModel

        val javaFile = psiFile as PsiJavaFile
        val javaClassName = javaFile.name.replace(JAVA_EXTENSION, "")
        val packageName = javaFile.packageName

        val methods = PsiTreeUtil.findChildrenOfType(javaFile, PsiMethod::class.java)

        for (method in methods) {
            val methodName = method.name
            val parameters = method.parameterList.parameters.joinToString(",") { param ->
                param.type.presentableText.replace(Regex("<.*>"), "") // Remove the generic part
            }

            val isUsage: Boolean? = snapshotNodeMap["$packageName.$javaClassName.$methodName($parameters)"]

            log.debug("Full signature : $packageName.$javaClassName.$methodName($parameters), Usage : $isUsage")

            val color = if (isUsage ?: continue) JBColor(USAGE_COLOR, USAGE_COLOR) else JBColor(UNUSED_COLOR, UNUSED_COLOR)
            val attributes = TextAttributes().apply { backgroundColor = color }

            val startOffset = method.textRange.startOffset
            val endOffset = method.textRange.endOffset
            markupModel.addRangeHighlighter(startOffset, endOffset, HIGHTLIGHER_LAYER, attributes, HighlighterTargetArea.EXACT_RANGE)
        }
    }
}