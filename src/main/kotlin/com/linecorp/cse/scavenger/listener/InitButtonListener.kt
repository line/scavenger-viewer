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

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.INPUT_URL_TEXT
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.INPUT_URL_TITLE
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SETTING_COMPLETED_MESSAGE
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SETTING_NOTIFICATION_GROUP_ID
import com.linecorp.cse.scavenger.domain.ScavengerInfo
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log
import com.linecorp.cse.scavenger.window.ScavengerToolWindow
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class InitButtonListener(
    private val project: Project,
    private val scavengerToolWindow: ScavengerToolWindow,
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        log.info("button action listener")
        val baseUrl = ScavengerInfo.getBaseUrl()
        val inputBaseUrl = Messages.showInputDialog(project, INPUT_URL_TEXT, INPUT_URL_TITLE, Messages.getQuestionIcon(), baseUrl, null)

        if (inputBaseUrl != null) {
            if (inputBaseUrl.isNotBlank()) {
                ScavengerInfo.saveBaseUrl(inputBaseUrl)

                NotificationGroupManager.getInstance()
                    .getNotificationGroup(SETTING_NOTIFICATION_GROUP_ID)
                    .createNotification(SETTING_COMPLETED_MESSAGE, NotificationType.INFORMATION)
                    .notify(project)
            } else {
                // User removed the base URL
                ScavengerInfo.resetSavedData()
            }

            scavengerToolWindow.setCustomerNodes()
        }
    }
}