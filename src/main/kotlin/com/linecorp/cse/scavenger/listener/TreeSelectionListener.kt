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
import com.linecorp.cse.scavenger.cache.SnapshotNodeMapCache
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SNAPSHOT_SELECTED_MESSAGE
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SNAPSHOT_SELECTED_NOTIFICATION_GROUP_ID
import com.linecorp.cse.scavenger.domain.Customer
import com.linecorp.cse.scavenger.domain.ScavengerInfo
import com.linecorp.cse.scavenger.domain.Snapshot
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log
import com.linecorp.cse.scavenger.service.EditorHighlighterService
import com.linecorp.cse.scavenger.window.ScavengerToolWindow
import javax.swing.event.TreeSelectionEvent
import javax.swing.tree.DefaultMutableTreeNode


class TreeSelectionListener(
    private val project: Project,
    private val scavengerToolWindow: ScavengerToolWindow,
) : javax.swing.event.TreeSelectionListener {

    private val editorHighlighterService = project.getService(EditorHighlighterService::class.java)
    private val cache = project.getService(SnapshotNodeMapCache::class.java)

    override fun valueChanged(e: TreeSelectionEvent) {
        log.info("tree action listener")
        val selectedNode = e.path.lastPathComponent as DefaultMutableTreeNode
        val userObject = selectedNode.userObject
        if (userObject is Customer && ScavengerInfo.getBaseUrl() != null) {
            log.info("Customer click")

            ScavengerInfo.saveCustomerId(userObject.id)
            ScavengerInfo.removeSnapshotId()
            scavengerToolWindow.setSnapshotNodes()

        } else if (userObject is Snapshot) {
            log.info("Snapshot click")

            checkAndResetCache(userObject)

            ScavengerInfo.saveSnapshotId(userObject.id)
            ScavengerInfo.saveCustomerId(userObject.customerId)

            val scavengerOn = ScavengerInfo.isOnScavenger()
            if (scavengerOn) {
                editorHighlighterService.highlightMethods()
            }

            log.info("SelectedSnapShot: {}", ScavengerInfo.getSnapshotId())
            NotificationGroupManager.getInstance()
                .getNotificationGroup(SNAPSHOT_SELECTED_NOTIFICATION_GROUP_ID)
                .createNotification(SNAPSHOT_SELECTED_MESSAGE, NotificationType.INFORMATION)
                .notify(project)
        }
    }

    private fun checkAndResetCache(snapshot: Snapshot) {
        if (ScavengerInfo.getCustomerId() == snapshot.customerId
            && ScavengerInfo.getSnapshotId() != snapshot.id
            && ScavengerInfo.getSnapshotId() != null
        ) {
            cache.removeAll()
        }
    }
}