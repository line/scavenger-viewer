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
package com.linecorp.cse.scavenger.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal val scavengerPluginDefaultTimeZone = ZoneId.of(ZoneId.SHORT_IDS["JST"])
fun Long.epochSecond2LocalDateTime(): LocalDateTime {
    require(this != Long.MAX_VALUE) { "Long.MaxValue should not convert." }
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(this), scavengerPluginDefaultTimeZone)
}

fun Long.epochMillis2LocalDateTime(): LocalDateTime {
    require(this != Long.MAX_VALUE) { "Long.MaxValue should not convert." }
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), scavengerPluginDefaultTimeZone)
}

fun LocalDateTime.toEpochMilli(): Long {
    return atZone(scavengerPluginDefaultTimeZone).toInstant().toEpochMilli()
}

fun concatenateStrings(vararg args: String): String {
    return args.filter { it.isNotEmpty() }.joinToString(",")
}

fun transformString(input: String): String {
    val regex = Regex("""(\w+(?:\.\w+)+)\(([^)]+)\)""")
    val matchResult = regex.find(input) ?: return input

    val fullMethodName = matchResult.groupValues[1]
    val params = matchResult.groupValues[2]

    val transformedParams = params.split(",").map { it.substringAfterLast('.') }.joinToString(",")

    return "$fullMethodName($transformedParams)"
}