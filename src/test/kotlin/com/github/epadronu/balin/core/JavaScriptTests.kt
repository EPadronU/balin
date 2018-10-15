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
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert.assertEquals
import org.testng.Assert.expectThrows
import org.testng.Assert.fail
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit
/* ***************************************************************************/

/* ***************************************************************************/
class JavaScriptTests {

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create the no JavaScript-enabled WebDriver`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BrowserVersion.FIREFOX_52) })
    )

    @DataProvider(name = "JavaScript-enabled WebDriver factory", parallel = true)
    fun `Create the JavaScript-enabled WebDriver`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BrowserVersion.FIREFOX_52).apply { isJavascriptEnabled = true } })
    )

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Execute valid JavaScript code in a JS-incapable browser`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute valid JavaScript code
            // Then such execution should be a failure
            expectThrows(UnsupportedOperationException::class.java) {
                js.call { "console.log(\"Hello, world!\")" }
            }
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute valid JavaScript code in the browser`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            try {
                // When I execute a simple `console.log`
                js.call { "console.log(\"Hello, world!\")" }
            } catch (exception: ScriptException) {
                // Then such execution should be successful
                fail(exception.message)
            }
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute invalid JavaScript code in the browser`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute an invalid JavaScript code
            // Then such execution should be a failure
            expectThrows(ScriptException::class.java) {
                js.call { "an obvious bad JavaScript code" }
            }
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute JavaScript code with arguments via the call method`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with arguments via `call`
            val arguments = js.call(1, 2) {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals(arguments, "Arguments: 1, 2")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute JavaScript code with arguments via the execute method`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with arguments via `execute`
            val arguments = js.execute(true, false) {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals(arguments, "Arguments: true, false")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute JavaScript code with arguments via the run method`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with arguments via `run`
            val arguments = js.run("a", "b") {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals(arguments, "Arguments: a, b")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute JavaScript code with arguments via the invoke operator`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with arguments via `invoke`
            val arguments = js(1.5, 2.3) {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals(arguments, "Arguments: 1.5, 2.3")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute JavaScript code with a WebElement as its argument and interact with it`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute JavaScript code with a WebElement as its argument and return its content
            val text = js(`$`(".kotlin-info-description", 0)) {
                "return arguments[0].textContent.replace(/\\n +/g, ' ').trim()"
            }

            // Then I should get such content as expected
            assertEquals(text, "Watch KotlinConf 2018 Opening Keynote")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Set a global JS variable and retrieve it via the execute method`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I set a global variable
            js["myGlobal"] = "global variable"

            // And I retrieve such global variable via the `execute` method
            val globalVariableValue = js.execute { "return window.myGlobal;" }

            // Then I should get the variable's value as is
            assertEquals(globalVariableValue, "global variable")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Set a global JS variable and retrieve it via a get`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // Given I navigate to a page
            to("http://kotlinlang.org/")

            // When I set a global variable
            js["myGlobal"] = "super global variable"

            // And I retrieve such global variable via a `get` call
            val globalVariableValue = js["myGlobal"]

            // Then I should get the variable's value as is
            assertEquals(globalVariableValue, "super global variable")
        }
    }

    @Test
    fun `Execute an asynchronous JavaScript code`() {
        // Given I tell the driver to wait a second for a script to terminate
        val driverFactory = {
            HtmlUnitDriver(BrowserVersion.FIREFOX_52).apply {
                isJavascriptEnabled = true

                manage().timeouts().setScriptTimeout(1L, TimeUnit.SECONDS)
            }
        }

        // And I create a 100-elements array to be passed as arguments to the script
        val arguments = Array(100) { it }

        Browser.drive(driverFactory = driverFactory) {
            // And I navigate to a page
            to("http://kotlinlang.org/")

            // When I execute an asynchronous JS code to get how many arguments I passed to it
            val argumentsLength = js(*arguments, async = true) {
                """
                    var argumentsLength = arguments.length - 1;

                    var callback = arguments[arguments.length - 1];

                    window.setTimeout(function() { callback(argumentsLength); }, 500);
                """
            }

            // Then I should get the expected length
            assertEquals(argumentsLength, 100L)
        }
    }
}
/* ***************************************************************************/
