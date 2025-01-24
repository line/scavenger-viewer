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

import com.intellij.openapi.project.Project
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.OFF
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.ON
import com.linecorp.cse.scavenger.domain.ScavengerInfo
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log
import com.linecorp.cse.scavenger.service.EditorHighlighterService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton

class OnOffButtonListener(
    private val project: Project,
    private val onOffButton: JButton,
) : ActionListener {
    private val editorHighlighterService = project.getService(EditorHighlighterService::class.java)

    override fun actionPerformed(e: ActionEvent?) {
        log.info("button action listener")

        ScavengerInfo.toggleScavengerOnOff()

        val scavengerOn = ScavengerInfo.isOnScavenger()
        onOffButton.text = if (scavengerOn) ON else OFF

        if (!scavengerOn) {
            editorHighlighterService.removeHighlighters()
        } else {
            editorHighlighterService.highlightMethods()
        }
    }
}