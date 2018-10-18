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
 * Describes an easier way to interact with
 * [JavascriptExecutor.executeScript][org.openqa.selenium.JavascriptExecutor.executeScript] &
 * [JavascriptExecutor.executeAsyncScript][org.openqa.selenium.JavascriptExecutor.executeAsyncScript],
 * allowing the execution of synchronous and asynchronous JavaScript code if
 * such functionality is supported by the underground driver.
 *
 * ### Synchronous code
 * @sample com.github.epadronu.balin.core.JavaScriptTests.execute_javaScript_code_with_arguments_via_the_invoke_operator
 *
 * ### Asynchronous code
 * @sample com.github.epadronu.balin.core.JavaScriptTests.execute_an_asynchronous_javascript_code
 */
interface JavaScriptExecutor {

    /**
     * Executes JavaScript in the context of the currently selected frame or
     * window. The script fragment provided will be executed as the body of an
     * anonymous function.
     *
     * Within the script, use document to refer to the current document. Note
     * that local variables will not be available once the script has finished
     * executing, though global variables will persist.
     *
     * If the script has a return value (i.e. if the script contains a return
     * statement), then the following steps will be taken:
     *
     * - For an HTML element, this method returns a WebElement
     * - For a decimal, a Double is returned
     * - For a non-decimal number, a Long is returned
     * - For a boolean, a Boolean is returned
     * - For all other cases, a String is returned.
     * - For an array, return a List<Object> with each object following the rules above. We support nested lists.
     * - For a map, return a Map<String, Object> with values following the rules above.
     * - Unless the value is null or there is no return value, in which null is returned
     *
     * Arguments must be a number, a boolean, a String, WebElement, or a List
     * of any combination of the above. An exception will be thrown if the
     * arguments do not meet these criteria. The arguments will be made
     * available to the JavaScript via the "`arguments`" magic variable, as if
     * the function were called via "`Function.apply`"
     *
     * In the case of `async = true`, unlike executing synchronous JavaScript,
     * scripts executed with this method must explicitly signal they are
     * finished by invoking the provided callback. This callback is always
     * injected into the executed function as the last argument.
     *
     * The default timeout for a script to be executed is 0ms. In most cases,
     * including the examples below, one must set the script timeout
     * ([Timeouts.setScriptTimeout][org.openqa.selenium.WebDriver.Timeouts.setScriptTimeout])
     * beforehand to a value sufficiently large enough.
     *
     * @sample com.github.epadronu.balin.core.JavaScriptTests.execute_javaScript_code_with_arguments_via_the_execute_method
     * @see org.openqa.selenium.JavascriptExecutor.executeScript
     * @see org.openqa.selenium.JavascriptExecutor.executeAsyncScript
     *
     * @param args optional arguments that can be passed to the JS code
     * @param async indicates if the JS code should be executed asynchronously or not
     * @param script provides the JS code to be executed
     * @return One of Boolean, Long, Double, String, List, Map or WebElement. Or null.
     */
    fun execute(vararg args: Any, async: Boolean = false, script: () -> String): Any?

    /**
     * Executes JavaScript in the context of the currently selected frame or
     * window. The script fragment provided will be executed as the body of an
     * anonymous function.
     *
     * Within the script, use document to refer to the current document. Note
     * that local variables will not be available once the script has finished
     * executing, though global variables will persist.
     *
     * If the script has a return value (i.e. if the script contains a return
     * statement), then the following steps will be taken:
     *
     * - For an HTML element, this method returns a WebElement
     * - For a decimal, a Double is returned
     * - For a non-decimal number, a Long is returned
     * - For a boolean, a Boolean is returned
     * - For all other cases, a String is returned.
     * - For an array, return a List<Object> with each object following the rules above. We support nested lists.
     * - For a map, return a Map<String, Object> with values following the rules above.
     * - Unless the value is null or there is no return value, in which null is returned
     *
     * Arguments must be a number, a boolean, a String, WebElement, or a List
     * of any combination of the above. An exception will be thrown if the
     * arguments do not meet these criteria. The arguments will be made
     * available to the JavaScript via the "`arguments`" magic variable, as if
     * the function were called via "`Function.apply`"
     *
     * In the case of `async = true`, unlike executing synchronous JavaScript,
     * scripts must explicitly signal they are finished by invoking the
     * provided callback. This callback is always injected into the executed
     * function as the last argument.
     *
     * The default timeout for a script to be executed is 0ms. In most cases,
     * including the examples below, one must set the script timeout
     * ([Timeouts.setScriptTimeout][org.openqa.selenium.WebDriver.Timeouts.setScriptTimeout])
     * beforehand to a value sufficiently large enough.
     *
     * @sample com.github.epadronu.balin.core.JavaScriptTests.execute_javaScript_code_with_arguments_via_the_call_method
     * @see org.openqa.selenium.JavascriptExecutor.executeScript
     * @see org.openqa.selenium.JavascriptExecutor.executeAsyncScript
     *
     * @param args optional arguments that can be passed to the JS code
     * @param async indicates if the JS code should be executed asynchronously or not
     * @param script provides the JS code to be executed
     * @return One of Boolean, Long, Double, String, List, Map or WebElement. Or null.
     */
    fun call(vararg args: Any, async: Boolean = false, script: () -> String): Any? = execute(
        *args, async = async, script = script
    )

    /**
     * Executes JavaScript in the context of the currently selected frame or
     * window. The script fragment provided will be executed as the body of an
     * anonymous function.
     *
     * Within the script, use document to refer to the current document. Note
     * that local variables will not be available once the script has finished
     * executing, though global variables will persist.
     *
     * If the script has a return value (i.e. if the script contains a return
     * statement), then the following steps will be taken:
     *
     * - For an HTML element, this method returns a WebElement
     * - For a decimal, a Double is returned
     * - For a non-decimal number, a Long is returned
     * - For a boolean, a Boolean is returned
     * - For all other cases, a String is returned.
     * - For an array, return a List<Object> with each object following the rules above. We support nested lists.
     * - For a map, return a Map<String, Object> with values following the rules above.
     * - Unless the value is null or there is no return value, in which null is returned
     *
     * Arguments must be a number, a boolean, a String, WebElement, or a List
     * of any combination of the above. An exception will be thrown if the
     * arguments do not meet these criteria. The arguments will be made
     * available to the JavaScript via the "`arguments`" magic variable, as if
     * the function were called via "`Function.apply`"
     *
     * In the case of `async = true`, unlike executing synchronous JavaScript,
     * scripts executed with this method must explicitly signal they are
     * finished by invoking the provided callback. This callback is always
     * injected into the executed function as the last argument.
     *
     * The default timeout for a script to be executed is 0ms. In most cases,
     * including the examples below, one must set the script timeout
     * ([Timeouts.setScriptTimeout][org.openqa.selenium.WebDriver.Timeouts.setScriptTimeout])
     * beforehand to a value sufficiently large enough.
     *
     * @sample com.github.epadronu.balin.core.JavaScriptTests.execute_javaScript_code_with_arguments_via_the_run_method
     * @see org.openqa.selenium.JavascriptExecutor.executeScript
     * @see org.openqa.selenium.JavascriptExecutor.executeAsyncScript
     *
     * @param args optional arguments that can be passed to the JS code
     * @param async indicates if the JS code should be executed asynchronously or not
     * @param script provides the JS code to be executed
     * @return One of Boolean, Long, Double, String, List, Map or WebElement. Or null.
     */
    fun run(vararg args: Any, async: Boolean = false, script: () -> String): Any? = execute(
        *args, async = async, script = script
    )

    /**
     * Executes JavaScript in the context of the currently selected frame or
     * window. The script fragment provided will be executed as the body of an
     * anonymous function.
     *
     * Within the script, use document to refer to the current document. Note
     * that local variables will not be available once the script has finished
     * executing, though global variables will persist.
     *
     * If the script has a return value (i.e. if the script contains a return
     * statement), then the following steps will be taken:
     *
     * - For an HTML element, this method returns a WebElement
     * - For a decimal, a Double is returned
     * - For a non-decimal number, a Long is returned
     * - For a boolean, a Boolean is returned
     * - For all other cases, a String is returned.
     * - For an array, return a List<Object> with each object following the rules above. We support nested lists.
     * - For a map, return a Map<String, Object> with values following the rules above.
     * - Unless the value is null or there is no return value, in which null is returned
     *
     * Arguments must be a number, a boolean, a String, WebElement, or a List
     * of any combination of the above. An exception will be thrown if the
     * arguments do not meet these criteria. The arguments will be made
     * available to the JavaScript via the "`arguments`" magic variable, as if
     * the function were called via "`Function.apply`"
     *
     * In the case of `async = true`, unlike executing synchronous JavaScript,
     * scripts executed with this method must explicitly signal they are
     * finished by invoking the provided callback. This callback is always
     * injected into the executed function as the last argument.
     *
     * The default timeout for a script to be executed is 0ms. In most cases,
     * including the examples below, one must set the script timeout
     * ([Timeouts.setScriptTimeout][org.openqa.selenium.WebDriver.Timeouts.setScriptTimeout])
     * beforehand to a value sufficiently large enough.
     *
     * @sample com.github.epadronu.balin.core.JavaScriptTests.execute_javaScript_code_with_arguments_via_the_invoke_operator
     * @see org.openqa.selenium.JavascriptExecutor.executeScript
     * @see org.openqa.selenium.JavascriptExecutor.executeAsyncScript
     *
     * @param args optional arguments that can be passed to the JS code
     * @param async indicates if the JS code should be executed asynchronously or not
     * @param script provides the JS code to be executed
     * @return One of Boolean, Long, Double, String, List, Map or WebElement. Or null.
     */
    operator fun invoke(vararg args: Any, async: Boolean = false, script: () -> String): Any? = execute(
        *args, async = async, script = script
    )

    /**
     * Get the value of a global-JavaScript variable.
     *
     * @sample com.github.epadronu.balin.core.JavaScriptTests.set_a_global_js_variable_and_retrieve_it_via_a_get
     *
     * @param value the name of the variable which value will be retrieved.
     * @return One of Boolean, Long, Double, String, List, Map or WebElement. Or null.
     */
    operator fun get(value: String): Any? = execute { "return $value;" }

    /**
     * Set the value of a global-JavaScript variable.
     *
     * @sample com.github.epadronu.balin.core.JavaScriptTests.set_a_global_js_variable_and_retrieve_it_via_a_get
     *
     * @param name the name of the variable.
     * @param value the value of the variable. (It can be null.)
     */
    operator fun set(name: String, value: Any?) {
        when (value) {
            null -> execute { "window.$name = null;" }
            else -> execute(value) { "window.$name = arguments[0];" }
        }
    }
}
/* ***************************************************************************/
