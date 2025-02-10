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
package com.linecorp.cse.scavenger.adapter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.components.Service
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SCAVENGER_CUSTOMER_API_PREFIX
import com.linecorp.cse.scavenger.const.ScavengerConst.Companion.SCAVENGER_SNAPSHOT_API_PATH
import com.linecorp.cse.scavenger.domain.Customer
import com.linecorp.cse.scavenger.domain.Snapshot
import com.linecorp.cse.scavenger.domain.SnapshotNode
import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log
import com.linecorp.cse.scavenger.utils.toEpochMilli
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service(Service.Level.PROJECT)
class ScavengerAdapter {

    private val client = OkHttpClient().newBuilder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()

    fun fetchCustomers(baseUrl: String): List<Customer> {
        val nowEpochTime = LocalDateTime.now().toEpochMilli()
        val path = "$baseUrl/${SCAVENGER_CUSTOMER_API_PREFIX}?_=${nowEpochTime}"
        val request = Request.Builder().url(path).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val type = object : TypeToken<List<Customer>>() {}.type
            return gson.fromJson(response.body?.charStream(), type)
        }
    }

    fun fetchSnapshots(baseUrl: String, customerId: Long): List<Snapshot> {
        log.info("fetchSnapshots")
        val path = "$baseUrl/${SCAVENGER_CUSTOMER_API_PREFIX}/$customerId/${SCAVENGER_SNAPSHOT_API_PATH}"
        val request = Request.Builder().url(path).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val type = object : TypeToken<List<Snapshot>>() {}.type
            return gson.fromJson(response.body?.charStream(), type)
        }
    }

    fun fetchSnapshotNodes(baseUrl: String, customerId: Long, snapshotId: Long, signature: String): List<SnapshotNode> {
        val url = "$baseUrl/$SCAVENGER_CUSTOMER_API_PREFIX/$customerId/$SCAVENGER_SNAPSHOT_API_PATH/$snapshotId?parent=$signature"
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val type = object : TypeToken<List<SnapshotNode>>() {}.type
            return gson.fromJson(response.body?.charStream(), type)
        }
    }
}