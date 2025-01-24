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
package com.linecorp.cse.scavenger.window

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.linecorp.cse.scavenger.listener.FileSelectionListener

class FileSelectionActivity : StartupActivity {
    override fun runActivity(project: Project) {
        val connection = project.messageBus.connect()
        val listener = project.getService(FileSelectionListener::class.java)
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listener)

        listener.initSelection()
    }
}