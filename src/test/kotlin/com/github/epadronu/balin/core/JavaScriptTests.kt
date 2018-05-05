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
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.ScriptException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class JavaScriptTests {

    private lateinit var driver: WebDriver

    @BeforeMethod
    fun `Create the JavaScript-enabled Web Driver`() {
        driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52).apply {
            isJavascriptEnabled = true
        }
    }

    @Test
    fun `Execute valid JavaScript code in the browser`() {
        // Given I navigate to a page
        Browser.drive(driver = driver) {
            to("http://kotlinlang.org/")

            // When I execute a simple `console.log`
            var itSucceed = true

            try {
                js.call { "console.log(\"Hello, world!\")" }
            } catch (ignore: ScriptException) {
                itSucceed = false
            }

            // Then such execution should be successful
            assertTrue(itSucceed)
        }
    }

    @Test
    fun `Execute invalid JavaScript code in the browser`() {
        // Given I navigate to a page
        Browser.drive(driver = driver) {
            to("http://kotlinlang.org/")

            // When I execute an invalid JavaScript code
            var itFailed = false

            try {
                js.call { "an obvious bad JavaScript code" }
            } catch (ignore: ScriptException) {
                itFailed = true
            }

            // Then such execution should be a failure
            assertTrue(itFailed)
        }
    }

    @Test
    fun `Execute JavaScript code with arguments via the call method`() {
        // Given I navigate to a page
        Browser.drive(driver = driver) {
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with arguments via `call`
            val arguments = js.call(1, 2) {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals("Arguments: 1, 2", arguments)
        }
    }

    @Test
    fun `Execute JavaScript code with arguments via the execute method`() {
        // Given I navigate to a page
        Browser.drive(driver = driver) {
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with arguments via `execute`
            val arguments = js.execute(true, false) {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals("Arguments: true, false", arguments)
        }
    }

    @Test
    fun `Execute JavaScript code with arguments via the run method`() {
        // Given I navigate to a page
        Browser.drive(driver = driver) {
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with arguments via `run`") {
            val arguments = js.run("a", "b") {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals("Arguments: a, b", arguments)
        }
    }

    @Test
    fun `Set a global JS variable and retrieve it via the execute method`() {
        // Given I navigate to a page
        Browser.drive(driver = driver) {
            to("http://kotlinlang.org/")

            // When I set a global variable
            js["myGlobal"] = "global variable"

            val globalVariableValue = js.execute { "return window.myGlobal;" }

            // Then I should get the variable's value as is
            assertEquals("global variable", globalVariableValue)
        }
    }

    @Test
    fun `Set a global JS variable and retrieve it via a get`() {
        // Given I navigate to a page
        Browser.drive(driver = driver) {
            to("http://kotlinlang.org/")

            // When I set a global variable
            js["myGlobal"] = "super global variable"

            val globalVariableValue = js["myGlobal"]

            // Then I should get the variable's value as is
            assertEquals("super global variable", globalVariableValue)
        }
    }
}
/* ***************************************************************************/
