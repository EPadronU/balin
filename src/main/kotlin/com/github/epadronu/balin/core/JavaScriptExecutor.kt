/******************************************************************************
 * Copyright 2016 Edinson E. Padrón Urdaneta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

/* ***************************************************************************/
package com.github.epadronu.balin.core
/* ***************************************************************************/

/* ***************************************************************************/
interface JavaScriptExecutor {

    fun execute(vararg args: Any, async: Boolean = false, script: () -> String): Any?

    fun call(vararg args: Any, async: Boolean = false, script: () -> String) = execute(
        *args, async = async, script = script
    )

    fun run(vararg args: Any, async: Boolean = false, script: () -> String) = execute(
        *args, async = async, script = script
    )

    operator fun invoke(vararg args: Any, async: Boolean = false, script: () -> String) = execute(
        *args, async = async, script = script
    )

    operator fun get(value: String) = execute { "return $value;" }

    operator fun set(field: String, value: Any?) {
        when (value) {
            null -> execute { "window.$field = null;" }
            else -> execute(value) { "window.$field = arguments[0];" }
        }
    }
}
/* ***************************************************************************/
