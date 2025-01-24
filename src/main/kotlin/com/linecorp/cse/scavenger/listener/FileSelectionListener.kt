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
package com.linecorp.cse.scavenger.listener

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.components.Service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.linecorp.cse.scavenger.adapter.ScavengerAdapter
import com.linecorp.cse.scavenger.cache.SnapshotNodeMapCache
import com.linecorp.cse.scavenger.domain.ScavengerInfo
import com.linecorp.cse.scavenger.domain.SnapshotNode
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log
import com.linecorp.cse.scavenger.service.*
import com.linecorp.cse.scavenger.utils.transformString
import org.jetbrains.kotlin.idea.KotlinFileType

@Service(Service.Level.PROJECT)
class FileSelectionListener(
    private val project: Project,
) : FileEditorManagerListener {

    private val cache = project.getService(SnapshotNodeMapCache::class.java)
    private val scavengerAdapter = project.getService(ScavengerAdapter::class.java)
    private val javaMethodHighlighter = project.getService(JavaMethodHighlighter::class.java)
    private val kotlinMethodHighlighter = project.getService(KotlinMethodHighlighter::class.java)
    private val javaClassSignatureExtractor = project.getService(JavaClassSignatureExtractor::class.java)
    private val kotlinClassSignatureExtractor = project.getService(KotlinClassSignatureExtractor::class.java)

    // initialize selection on intellij startup
    fun initSelection() {
        val selectedEditor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        val psiFile = PsiManager.getInstance(project).findFile(selectedEditor.virtualFile) ?: return

        processByFileType(psiFile)
    }

    // selection changed event listener
    override fun selectionChanged(event: FileEditorManagerEvent) {
        log.debug("selection changed")

        // TODO: Cache if the file is already processed
        val file = event.newFile ?: return
        val psiFile = PsiManager.getInstance(project).findFile(file) ?: return

        processByFileType(psiFile)
    }

    private fun processByFileType(psiFile: PsiFile) {
        val scavengerOn = ScavengerInfo.isOnScavenger()
        if (!scavengerOn) {
            return
        }
        when (psiFile.fileType) {
            is JavaFileType -> process(psiFile, project, javaMethodHighlighter, javaClassSignatureExtractor)
            is KotlinFileType -> process(psiFile, project, kotlinMethodHighlighter, kotlinClassSignatureExtractor)
        }
    }

    private fun process(
        psiFile: PsiFile,
        project: Project,
        highlighter: MethodHighlighter,
        extractor: ClassSignatureExtractor,
    ) {
        val selectedSnapShotId = ScavengerInfo.getSnapshotId() ?: return
        val selectedCustomerId = ScavengerInfo.getCustomerId() ?: return
        val baseUrl = ScavengerInfo.getBaseUrl() ?: return

        val clazzSignature = extractor.getClassSignature(psiFile) ?: return

        val snapshotNodes = cache.get(clazzSignature) ?: scavengerAdapter.fetchSnapshotNodes(
            baseUrl = baseUrl,
            customerId = selectedCustomerId,
            snapshotId = selectedSnapShotId,
            signature = clazzSignature
        ).also { cache.put(clazzSignature, it) }

        val snapshotNodeMap = snapshotNodes
            .asSequence()
            .filter { it.type == SnapshotNode.SnapshotType.METHOD }
            .associateBy({ transformString(it.signature) }, { it.usedCount != 0L })

        highlighter.highlightMethods(psiFile = psiFile, snapshotNodeMap = snapshotNodeMap, project = project)
    }
}
