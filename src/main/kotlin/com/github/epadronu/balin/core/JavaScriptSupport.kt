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
/**
 * Describes the `js` property support, which aims to ease the use of
 * [JavascriptExecutor.executeScript][org.openqa.selenium.JavascriptExecutor.executeScript] &
 * [JavascriptExecutor.executeAsyncScript][org.openqa.selenium.JavascriptExecutor.executeAsyncScript].
 *
 * @sample com.github.epadronu.balin.core.JavaScriptTests.execute_javaScript_code_with_arguments_via_the_invoke_operator
 */
interface JavaScriptSupport {

    /**
     * Allows the execution of synchronous and asynchronous JavaScript code if
     * such functionality is supported by the underground driver.
     */
    val js: JavaScriptExecutor
}
/* ***************************************************************************/
