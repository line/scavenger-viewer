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
package com.linecorp.cse.scavenger.const

class ScavengerConst {
    companion object {
        const val HIGHTLIGHER_LAYER: Int = 123
        const val ON = "On"
        const val OFF = "Off"

        const val BASE_URL = "baseUrl"
        const val CUSTOMER_ID = "customerId"
        const val SNAPSHOT_ID = "snapshotId"
        const val SCAVENGER_SWITCH = "scavenger.switch"
        const val SETTING_BASE_URL = "Setting Base URL"
        const val INPUT_URL_TEXT = "Enter base URL:"

        const val INPUT_URL_TITLE = "Input URL"
        const val SETTING_COMPLETED_MESSAGE = "Setting completed"
        const val SNAPSHOT_SELECTED_MESSAGE = "Snapshot Selected"

        const val SETTING_NOTIFICATION_GROUP_ID = "scavenger-setting-completed-notification"
        const val SNAPSHOT_SELECTED_NOTIFICATION_GROUP_ID = "scavenger-snapshot-selected-notification"

        const val SCAVENGER_CUSTOMER_API_PREFIX = "scavenger/api/customers"
        const val SCAVENGER_SNAPSHOT_API_PATH = "/snapshots"
    }


}