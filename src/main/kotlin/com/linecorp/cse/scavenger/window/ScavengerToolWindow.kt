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

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.linecorp.cse.scavenger.adapter.ScavengerAdapter
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.OFF
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.ON
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SETTING_BASE_URL
import com.linecorp.cse.scavenger.domain.Customer
import com.linecorp.cse.scavenger.domain.ScavengerInfo
import com.linecorp.cse.scavenger.listener.InitButtonListener
import com.linecorp.cse.scavenger.listener.OnOffButtonListener
import com.linecorp.cse.scavenger.listener.TreeSelectionListener
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreePath

@Service(Service.Level.PROJECT)
class ScavengerToolWindow(private val project: Project) {

    private val scavengerAdapter = project.getService(ScavengerAdapter::class.java)
    private val rootNode = DefaultMutableTreeNode("Scavenger")
    private val treeModel = DefaultTreeModel(rootNode)
    private val tree = JTree(treeModel)

    val content: JPanel = JBPanel<JBPanel<*>>().apply {
        layout = BorderLayout()

        add(JScrollPane(tree), BorderLayout.CENTER)

        val baseUrlButton = JButton(SETTING_BASE_URL)
        baseUrlButton.addActionListener(InitButtonListener(project, this@ScavengerToolWindow))

        val onOffButton = JButton(if (ScavengerInfo.isOnScavenger()) ON else OFF)
        onOffButton.addActionListener(OnOffButtonListener(project, onOffButton))

        val bottomPnl = JPanel(FlowLayout(FlowLayout.CENTER))
        bottomPnl.add(baseUrlButton)
        bottomPnl.add(onOffButton)

        add(bottomPnl, BorderLayout.SOUTH)

        tree.addTreeSelectionListener(TreeSelectionListener(project, this@ScavengerToolWindow))
    }

    fun loadSavedData() {
        setCustomerNodes()
        setSnapshotNodes()
    }

    fun setCustomerNodes() {
        ScavengerInfo.getBaseUrl()?.let {
            val customers = scavengerAdapter.fetchCustomers(it)

            rootNode.removeAllChildren()
            customers.forEach { customer ->
                val customerNode = DefaultMutableTreeNode(customer)
                rootNode.add(customerNode)
            }
            treeModel.reload()
        } ?: run {
            rootNode.removeAllChildren()
            treeModel.reload()
        }
    }

    fun setSnapshotNodes() {
        ScavengerInfo.getCustomerId()?.let {
            val customerNode = findNodeByCustomerId(it)

            val snapshots = scavengerAdapter.fetchSnapshots(ScavengerInfo.getBaseUrl()!!, it)
            customerNode?.removeAllChildren()
            var selectedNode: DefaultMutableTreeNode? = null
            snapshots.forEach { snapshot ->
                val snapshotNode = DefaultMutableTreeNode(snapshot)
                customerNode?.add(snapshotNode)

                if (snapshot.id == ScavengerInfo.getSnapshotId()) {
                    selectedNode = snapshotNode
                }
            }
            treeModel.reload()

            // Expand the customer node
            customerNode?.let { node ->
                tree.expandPath(TreePath(node.path))
            }

            // Select the snapshot node
            selectedNode?.let { node ->
                tree.selectionPath = TreePath(node.path)
            }
        }
    }

    private fun findNodeByCustomerId(customerId: Long): DefaultMutableTreeNode? {
        val root = treeModel.root as DefaultMutableTreeNode
        for (i in 0 until root.childCount) {
            val node = root.getChildAt(i) as DefaultMutableTreeNode
            if (node.userObject is Customer && (node.userObject as Customer).id == customerId) {
                return node
            }
        }
        return null
    }
}