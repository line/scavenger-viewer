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
package com.linecorp.cse.scavenger.domain

import com.intellij.ide.util.PropertiesComponent
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.BASE_URL
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.CUSTOMER_ID
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SCAVENGER_SWITCH
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SNAPSHOT_ID


class ScavengerInfo {
    companion object {
        fun saveBaseUrl(baseUrl: String) {
            val propertiesComponent = PropertiesComponent.getInstance()
            propertiesComponent.setValue(BASE_URL, baseUrl)
        }

        fun getBaseUrl(): String? {
            val propertiesComponent = PropertiesComponent.getInstance()
            val baseUrl = propertiesComponent.getValue(BASE_URL)

            return baseUrl
        }

        fun saveCustomerId(customerId: Long) {
            val propertiesComponent = PropertiesComponent.getInstance()
            propertiesComponent.setValue(CUSTOMER_ID, customerId.toString())
        }

        fun getCustomerId(): Long? {
            val propertiesComponent = PropertiesComponent.getInstance()
            val customerId = propertiesComponent.getValue(CUSTOMER_ID)

            return customerId?.toLongOrNull()
        }

        fun removeSnapshotId() {
            val propertiesComponent = PropertiesComponent.getInstance()
            propertiesComponent.unsetValue(SNAPSHOT_ID)
        }

        fun saveSnapshotId(snapshotId: Long) {
            val propertiesComponent = PropertiesComponent.getInstance()
            propertiesComponent.setValue(SNAPSHOT_ID, snapshotId.toString())
        }

        fun getSnapshotId(): Long? {
            val propertiesComponent = PropertiesComponent.getInstance()
            val snapshotId = propertiesComponent.getValue(SNAPSHOT_ID)

            return snapshotId?.toLongOrNull()
        }

        fun resetSavedData() {
            val propertiesComponent = PropertiesComponent.getInstance()
            propertiesComponent.unsetValue(SNAPSHOT_ID)
            propertiesComponent.unsetValue(CUSTOMER_ID)
            propertiesComponent.unsetValue(BASE_URL)
        }

        fun toggleScavengerOnOff() {
            val propertiesComponent = PropertiesComponent.getInstance()
            val scavengerOn = !propertiesComponent.getBoolean(SCAVENGER_SWITCH, true)
            propertiesComponent.setValue(SCAVENGER_SWITCH, scavengerOn.toString())
        }

        fun isOnScavenger(): Boolean {
            val propertiesComponent = PropertiesComponent.getInstance()
            return propertiesComponent.getBoolean(SCAVENGER_SWITCH, true)
        }
    }

}