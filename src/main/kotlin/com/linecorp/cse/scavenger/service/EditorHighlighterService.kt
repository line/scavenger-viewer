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
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.HIGHTLIGHER_LAYER
import com.linecorp.cse.scavenger.listener.FileSelectionListener

@Service(Service.Level.PROJECT)
class EditorHighlighterService(private val project: Project) {
    private val fileSelectionListener = project.getService(FileSelectionListener::class.java)
    fun highlightMethods() {
        fileSelectionListener.initSelection()
    }

    fun removeHighlighters() {
        val editors = FileEditorManager.getInstance(project).allEditors
        for (editor in editors) {
            if (editor is TextEditor) {
                val textEditor = editor as TextEditor
                val markupModel = textEditor.editor.markupModel
                val highlighters = markupModel.allHighlighters

                for (highlighter in highlighters) {
                    if (highlighter.layer == HIGHTLIGHER_LAYER) {
                        markupModel.removeHighlighter(highlighter)
                    }
                }
            }
        }
    }
}