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
import com.github.epadronu.balin.config.Configuration
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchSessionException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.Assert.assertThrows
import org.testng.Assert.assertTrue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class BrowserTests {

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create the no JavaScript-enabled WebDriver`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BrowserVersion.FIREFOX_52) })
    )

    @DataProvider(name = "JavaScript-enabled WebDriver factory", parallel = true)
    fun `Create the JavaScript-enabled WebDriver`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BrowserVersion.FIREFOX_52).apply { isJavascriptEnabled = true } })
    )

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Perform a simple web navigation`(driverFactory: () -> WebDriver) {
        // Given the Kotlin's website index page URL
        val indexPageUrl = "http://kotlinlang.org/"

        // When I visit such URL
        lateinit var currentBrowserUrl: String
        lateinit var currentPageTitle: String

        Browser.drive(driverFactory) {
            currentBrowserUrl = to(indexPageUrl)
            currentPageTitle = title
        }

        // Then I should change the browser's URL to the given one
        assertEquals(currentBrowserUrl, indexPageUrl)

        // And I should get the title of the Kotlin's website index page
        assertEquals(currentPageTitle, "Kotlin Programming Language")
    }

    @Test(description = "Find some basic elements in the page",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun find_some_basic_elements_in_the_page(driverFactory: () -> WebDriver) {
        // Given the Kotlin's website index page URL
        val indexPageUrl = "http://kotlinlang.org/"

        Browser.drive(driverFactory) {
            // When I visit such URL
            to(indexPageUrl)

            // Then I should get the different platforms Kotlin works on, in alphabetical order
            assertEquals(
                `$`("a.works-on-item").`$`(".works-on-text", 1, 2, 0, 3).map { it.text.trim() },
                listOf("Android", "Browser", "JVM", "Native")
            )

            // And I should get the Try-Kotlin section's description
            assertEquals(
                `$`("#get-kotlin").`$`("div", 1).text.replace("(?m)\\s+".toRegex(), " "),
                "Explore Kotlin code samples and solve problems directly in the browser")

            // And I should get the second and third stay-in-touch methods
            assertEquals(
                `$`(".links-list li a").`$`("span:nth-of-type(2)", 1..2).map { it.text },
                listOf("Community", "Twitter"))
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Model a page into a Page Object and interact with it via the at method`(driverFactory: () -> WebDriver) {
        // Given the Kotlin's website index page with content elements and no URL
        class IndexPage(browser: Browser) : Page(browser) {

            override val url: String? = null

            override val at = at {
                title == "Kotlin Programming Language"
            }

            val navItems by lazy {
                `$`("a.nav-item").map { it.text }
            }

            val tryItBtn by lazy {
                `$`(".try-button", 0).text
            }

            val features by lazy {
                `$`("li.kotlin-feature").`$`("h3:nth-child(2)").map { it.text }
            }
        }

        // When I visit the index page URL and set the browser's page with `at`
        var page: IndexPage? = null

        Browser.drive(driverFactory) {
            to("http://kotlinlang.org/")

            page = at(::IndexPage).apply {
                // In order to execute the lazy evaluation and cache the results
                features; navItems; tryItBtn
            }
        }

        // The I should change the browser's page to the given one
        assertEquals(page is IndexPage, true)

        // And I should get the navigation items
        assertEquals(page?.navItems, listOf("Learn", "Community", "Try Online"))

        // And I should get the try-it button
        assertEquals(page?.tryItBtn, "Try online")

        // And I should get the coolest features
        assertEquals(
            page?.features, listOf("Concise", "Safe", "Interoperable", "Tool-friendly")
        )
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Wait for the presence of an element that should be there`(driverFactory: () -> WebDriver) {
        // Given the selector of an element that should be present
        val locator = By.cssSelector(".global-header-logo")

        // When I wait for the element located by such selector to be present
        var itSucceed = true

        try {
            Browser.drive(driverFactory) {
                to("http://kotlinlang.org/")

                waitFor {
                    ExpectedConditions.presenceOfElementLocated(locator)
                }
            }
        } catch (ignore: TimeoutException) {
            itSucceed = false
        }

        // Then I should wait until the element appears in the page
        assertTrue(itSucceed)
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Wait for the presence of an element that shouldn't be there`(driverFactory: () -> WebDriver) {
        // Given the selector of an element that shouldn't be present
        val locator = By.cssSelector("#wrong.selector")

        // When I wait for the element located by such selector to be present
        var itFailed = false

        try {
            Browser.drive(driverFactory) {
                to("http://kotlinlang.org/")

                waitFor {
                    ExpectedConditions.presenceOfElementLocated(locator)
                }
            }
        } catch (ignore: TimeoutException) {
            itFailed = true
        }

        // Then I should reach the time limit since the element won't ever be there
        assertTrue(itFailed)
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Wait for the presence of a dynamic element with a well-chosen sleep and timeout times`(driverFactory: () -> WebDriver) {
        // Given a configuration with a well-chosen sleep and timeout times
        val desiredConfiguration = Configuration(
            driverFactory = driverFactory, waitForSleepTimeInMilliseconds = 500L, waitForTimeOutTimeInSeconds = 2L)

        // And the locator for the dynamic element
        val locator = By.cssSelector("#balin-test-target")

        var itFailed = false

        Browser.drive(desiredConfiguration) {
            // When I navigate to a page
            to("http://kotlinlang.org/")

            // And I create a dynamic element after certain period of time
            js {
                """
                    var body = document.querySelector('body');
                    var targetElement = document.createElement("div");

                    targetElement.id = "balin-test-target";

                    setTimeout(function() { body.appendChild(targetElement); }, 1000);
                """.trimIndent()
            }

            // And I use the waitFor function for obtaining the dynamic element
            try {
                waitFor {
                    ExpectedConditions.presenceOfAllElementsLocatedBy(locator)
                }
            } catch (ignore: TimeoutException) {
                itFailed = true
            }

            // Then I should find the element
            assertFalse(itFailed)
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Wait for the presence of a dynamic element with a short timeout time`(driverFactory: () -> WebDriver) {
        // Given a configuration with a short timeout time
        val desiredConfiguration = Configuration(
            driverFactory = driverFactory, waitForSleepTimeInMilliseconds = 500L, waitForTimeOutTimeInSeconds = 1L)

        // And the locator for the dynamic element
        val locator = By.cssSelector("#balin-test-target")

        var itFailed = false

        Browser.drive(desiredConfiguration) {
            // When I navigate to a page
            to("http://kotlinlang.org/")

            // And I create a dynamic element after certain period of time
            js {
                """
                    var body = document.querySelector('body');
                    var targetElement = document.createElement("div");

                    targetElement.id = "balin-test-target";

                    setTimeout(function() { body.appendChild(targetElement); }, 1100);
                """.trimIndent()
            }

            // And I use the waitFor function for obtaining the dynamic element
            try {
                waitFor {
                    ExpectedConditions.presenceOfAllElementsLocatedBy(locator)
                }
            } catch (ignore: TimeoutException) {
                itFailed = true
            }

            // Then I should reach the time limit since it is very short
            assertTrue(itFailed)
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Wait for the presence of a dynamic element with a favorable long sleep time time`(driverFactory: () -> WebDriver) {
        // Given a configuration with a favorable long sleep time
        val desiredConfiguration = Configuration(
            driverFactory = driverFactory, waitForSleepTimeInMilliseconds = 2000L, waitForTimeOutTimeInSeconds = 1L)

        // And the locator for the dynamic element
        val locator = By.cssSelector("#balin-test-target")

        var itFailed = false

        Browser.drive(desiredConfiguration) {
            // When I navigate to a page
            to("http://kotlinlang.org/")

            // And I create a dynamic element after certain period of time
            js {
                """
                    var body = document.querySelector('body');
                    var targetElement = document.createElement("div");

                    targetElement.id = "balin-test-target";

                    setTimeout(function() { body.appendChild(targetElement); }, 1500);
                """.trimIndent()
            }

            // And I use the waitFor function for obtaining the dynamic element
            try {
                waitFor {
                    ExpectedConditions.presenceOfAllElementsLocatedBy(locator)
                }
            } catch (ignore: TimeoutException) {
                itFailed = true
            }

            // Then I should find the element
            assertFalse(itFailed)
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Wait for the presence of a dynamic element with an unfavorable short sleep time time`(driverFactory: () -> WebDriver) {
        // Given a configuration with an unfavorable short sleep time
        val desiredConfiguration = Configuration(
            driverFactory = driverFactory, waitForSleepTimeInMilliseconds = 500L, waitForTimeOutTimeInSeconds = 1L)

        // And the locator for the dynamic element
        val locator = By.cssSelector("#balin-test-target")

        var itFailed = false

        Browser.drive(desiredConfiguration) {
            // When I navigate to a page
            to("http://kotlinlang.org/")

            // And I create a dynamic element after certain period of time
            js {
                """
                    var body = document.querySelector('body');
                    var targetElement = document.createElement("div");

                    targetElement.id = "balin-test-target";

                    setTimeout(function() { body.appendChild(targetElement); }, 1500);
                """.trimIndent()
            }

            // And I use the waitFor function for obtaining the dynamic element
            try {
                waitFor {
                    ExpectedConditions.presenceOfAllElementsLocatedBy(locator)
                }
            } catch (ignore: TimeoutException) {
                itFailed = true
            }

            // Then I should reach the time limit since both, the sleep and timeout times are short
            assertTrue(itFailed)
        }
    }

    @Test
    fun `The driver should quit even after an exception is thrown in the middle of the navigation`() {
        // Given the driver under test
        val driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52)

        // And an URL
        val indexPageUrl = "http://kotlinlang.org/"

        // When I navigate to such URL
        assertThrows(WebDriverException::class.java) {
            Browser.drive({ driver }, autoQuit = true) {
                to(indexPageUrl)

                // And I throw an exception in the middle of the navigation
                throw WebDriverException("This should'n prevent the browser to quit the driver")
            }
        }

        // And I ask for the title of the page
        // Then a NoSuchSessionException should be thrown since the driver should had quited
        assertThrows(NoSuchSessionException::class.java) {
            driver.title
        }
    }
}
/* ***************************************************************************/
