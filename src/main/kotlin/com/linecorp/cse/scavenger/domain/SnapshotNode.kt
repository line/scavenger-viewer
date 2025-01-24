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

data class SnapshotNode(
    val id: Long,
    val snapshotId: Long,
    val signature: String,
    val type: SnapshotType,
    val lastInvokedAtMillis: Long? = null,
    val parent: String,
    val usedCount: Long,
    val unusedCount: Long,
    val customerId: Long,
) {
    enum class SnapshotType {
        PACKAGE, CLASS, METHOD
    }
}