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
package com.linecorp.cse.scavenger.logging

import com.linecorp.cse.scavenger.logging.LoggingSupport.Companion.log
import mu.KotlinLogging
import org.slf4j.Logger

class LoggingSupport {
    companion object {
        val <reified T> T.log: Logger
            inline get() = KotlinLogging.logger { T::class.java.name }
    }
}

fun main() {
    val logger = LoggingSupport.log
    logger.info("This is an info log message")
    logger.debug("This is a debug log message")
    logger.warn("This is a warn log message")
    logger.error("This is an error log message")
}