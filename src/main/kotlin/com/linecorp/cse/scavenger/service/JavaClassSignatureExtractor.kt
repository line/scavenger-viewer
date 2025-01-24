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
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.util.PsiTreeUtil
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log

@Service(Service.Level.PROJECT)
class JavaClassSignatureExtractor : ClassSignatureExtractor {
    override fun getClassSignature(psiFile: PsiFile): String? {
        val javaFile = psiFile as PsiJavaFile
        val packageName = javaFile.packageName
        val classes = PsiTreeUtil.findChildrenOfType(javaFile, PsiClass::class.java)

        if (classes.isEmpty()) {
            log.error("No classes found in the Java file.")
            return null
        }

        val psiClass: PsiClass = classes.first()
        val className = psiClass.name

        return "$packageName.$className"
    }
}