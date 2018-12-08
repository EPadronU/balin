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
import com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX_60 as BROWSER_VERSION
/* ***************************************************************************/

/* ***************************************************************************/
class JavaScriptTests {

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create a JavaScript-incapable WebDriver factory`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BROWSER_VERSION) })
    )

    @DataProvider(name = "JavaScript-enabled WebDriver factory", parallel = true)
    fun `Create a JavaScript-enabled WebDriver factory`() = arrayOf(
        arrayOf({
            HtmlUnitDriver(BROWSER_VERSION).apply {
                isJavascriptEnabled = true

                manage().timeouts().setScriptTimeout(2L, TimeUnit.SECONDS)
            }
        })
    )

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Execute valid JavaScript code in a JS-incapable browser`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute valid JavaScript code
            // Then such execution should be a failure
            expectThrows(UnsupportedOperationException::class.java) {
                js.call { "console.log(\"Hello, world!\")" }
            }
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Execute valid JavaScript code in the browser`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            try {
                // And I execute a simple `console.log`
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
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute an invalid JavaScript code
            // Then such execution should be a failure
            expectThrows(ScriptException::class.java) {
                js.call { "an obvious bad JavaScript code" }
            }
        }
    }

    @Test(description = "Execute JavaScript code with arguments via the call method",
        dataProvider = "JavaScript-enabled WebDriver factory")
    fun execute_javaScript_code_with_arguments_via_the_call_method(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute JavaScript code with arguments via `call`
            val arguments = js.call(1, 2) {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals(arguments, "Arguments: 1, 2")
        }
    }

    @Test(description = "Execute JavaScript code with arguments via the execute method",
        dataProvider = "JavaScript-enabled WebDriver factory")
    fun execute_javaScript_code_with_arguments_via_the_execute_method(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute JavaScript code with arguments via `execute`
            val arguments = js.execute(true, false) {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals(arguments, "Arguments: true, false")
        }
    }

    @Test(description = "Execute JavaScript code with arguments via the run method",
        dataProvider = "JavaScript-enabled WebDriver factory")
    fun execute_javaScript_code_with_arguments_via_the_run_method(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute JavaScript code with arguments via `run`
            val arguments = js.run("a", "b") {
                "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
            }

            // Then I should get the arguments as is
            assertEquals(arguments, "Arguments: a, b")
        }
    }

    @Test(description = "Execute JavaScript code with arguments via the invoke operator",
        dataProvider = "JavaScript-enabled WebDriver factory")
    fun execute_javaScript_code_with_arguments_via_the_invoke_operator(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute JavaScript code with arguments via `invoke`
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
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute JavaScript code with a WebElement as its argument and return its content
            val text = js(`$`(".terms-copyright", 0)) {
                "return arguments[0].textContent.replace(/\\n +/g, ' ').trim()"
            }

            // Then I should get such content as expected
            assertEquals(text, "Licensed under the Apache 2 license")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Set a global JS variable and retrieve it via the execute method`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I set a global variable
            js["myGlobal"] = "global variable"

            // And I retrieve such global variable via the `execute` method
            val globalVariableValue = js.execute { "return window.myGlobal;" }

            // Then I should get the variable's value as is
            assertEquals(globalVariableValue, "global variable")
        }
    }

    @Test(description = "Set a global JS variable and retrieve it via a get",
        dataProvider = "JavaScript-enabled WebDriver factory")
    fun set_a_global_js_variable_and_retrieve_it_via_a_get(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I set a global variable
            js["myGlobal"] = "super global variable"

            // And I retrieve such global variable via a `get` call
            val globalVariableValue = js["myGlobal"]

            // Then I should get the variable's value as is
            assertEquals(globalVariableValue, "super global variable")
        }
    }

    @Test(description = "Execute an asynchronous JavaScript code",
        dataProvider = "JavaScript-enabled WebDriver factory")
    fun execute_an_asynchronous_javascript_code(driverFactory: () -> WebDriver) {
        // Given I create a 100-elements array to be passed as arguments to the script
        val arguments = Array(100) { it }

        Browser.drive(driverFactory = driverFactory) {
            // When I navigate to the Kotlin's page URL
            to("https://kotlinlang.org/")

            // And I execute an asynchronous JS code to get how many arguments I passed to it
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
