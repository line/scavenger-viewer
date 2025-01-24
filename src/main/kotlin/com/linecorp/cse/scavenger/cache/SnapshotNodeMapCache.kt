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
package com.linecorp.cse.scavenger.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.intellij.openapi.components.Service
import com.linecorp.cse.scavenger.domain.SnapshotNode
import java.util.concurrent.TimeUnit

@Service(Service.Level.PROJECT)
class SnapshotNodeMapCache : LocalCacheManager<String, List<SnapshotNode>> {

    private val cache: Cache<String, List<SnapshotNode>> = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterAccess(1, TimeUnit.DAYS)
        .build()

    override fun put(key: String, value: List<SnapshotNode>) {
        cache.put(key, value)
    }

    override fun putAll(resources: Map<out String, List<SnapshotNode>>) {
        cache.putAll(resources)
    }

    override fun get(key: String): List<SnapshotNode>? {
        return cache.getIfPresent(key)
    }

    override fun removeAll() {
        cache.invalidateAll()
    }
}