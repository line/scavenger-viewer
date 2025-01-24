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
import com.intellij.psi.PsiFile
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile

@Service(Service.Level.PROJECT)
class KotlinClassSignatureExtractor : ClassSignatureExtractor {
    override fun getClassSignature(psiFile: PsiFile): String? {
        val ktFile = psiFile as KtFile
        val packageName = ktFile.packageFqName.asString()

        val classes = ktFile.declarations.filterIsInstance<KtClass>()
        if (classes.isEmpty()) {
            log.error("No classes found in the Kotlin file.")
            return null
        }

        val ktClass = classes.first()
        val className = ktClass.name

        return "$packageName.$className"
    }
}