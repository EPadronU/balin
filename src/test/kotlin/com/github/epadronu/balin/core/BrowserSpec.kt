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
import com.github.epadronu.balin.libs.delegatesTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.openqa.selenium.By
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated
import kotlin.test.assertEquals

/* ***************************************************************************/

/* ***************************************************************************/
class BrowserSpec : Spek({
    given("the Kotlin's website index page URL") {
        val indexPageUrl = "http://kotlinlang.org/"

        on("visiting such URL") {
            var currentBrowserUrl: String? = null
            var currentPageTitle: String? = null

            Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                currentBrowserUrl = to(indexPageUrl)
                currentPageTitle = title
            }

            it("should change the browser's URL to the given one") {
                assertEquals(indexPageUrl, currentBrowserUrl)
            }

            it("should get the title of the Kotlin's website index page") {
                assertEquals("Kotlin Programming Language", currentPageTitle)
            }
        }
    }

    given("the Kotlin's website index page URL and a couple of CSS selectors") {
        val indexPageUrl = "http://kotlinlang.org/"

        val bonusFeaturesSelector = "li.kotlin-feature > h3:nth-child(2)"
        val coolestFeaturesSelector = "li.kotlin-feature > h3:nth-child(2)"
        val navItemsSelector = "a.nav-item"
        val tryItBtnSelector = ".get-kotlin-button"

        on("visiting such URL and getting the elements for said selectors") {
            var bonusFeatures: List<String>? = null
            var coolestFeatures: List<String>? = null
            var navItems: List<String>? = null
            var tryItBtn: String? = null

            Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                to(indexPageUrl)

                bonusFeatures = `$`(bonusFeaturesSelector, 4, 3).map { it.text }
                coolestFeatures = `$`(coolestFeaturesSelector, 0..2).map { it.text }
                navItems = `$`(navItemsSelector).map { it.text }
                tryItBtn = `$`(tryItBtnSelector, 0).text
            }

            it("should get the navigation items") {
                assertEquals(listOf("Learn", "Community", "Try Online"), navItems)
            }

            it("should get the try-it button") {
                assertEquals("Try Kotlin", tryItBtn)
            }

            it("should get the coolest features") {
                assertEquals(listOf("Concise", "Safe", "Versatile"), coolestFeatures)
            }

            it("should get the bonus features") {
                assertEquals(listOf("Tooling", "Interoperable"), bonusFeatures)
            }
        }
    }

    given("the Kotlin's website index page with content elements and no URL") {
        class IndexPage : Page() {
            override val url = null

            override val at = delegatesTo<Browser, Boolean> {
                title == "Kotlin Programming Language"
            }

            val navItems by lazy {
                `$`("a.nav-item").map { it.text }
            }

            val tryItBtn by lazy {
                `$`(".get-kotlin-button", 0).text
            }

            val coolestFeatures by lazy {
                `$`("li.kotlin-feature").`$`("h3:nth-child(2)", 0..2).map { it.text }
            }

            val bonusFeatures by lazy {
                `$`("li.kotlin-feature").`$`("h3:nth-child(2)", 4, 3).map { it.text }
            }
        }

        on("visiting the index page URL and setting the browser's page with `at`") {
            var page: IndexPage? = null

            Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                to("http://kotlinlang.org/")

                page = at(::IndexPage).apply {
                    // In order to execute the lazy evaluation and cache the results
                    bonusFeatures; coolestFeatures; navItems; tryItBtn
                }
            }

            it("should change the browser's page to the given one") {
                assertEquals(true, page is IndexPage)
            }

            it("should get the navigation items") {
                assertEquals(
                        listOf("Learn", "Community", "Try Online"), page?.navItems
                )
            }

            it("should get the try-it button") {
                assertEquals("Try Kotlin", page?.tryItBtn)
            }

            it("should get the coolest features") {
                assertEquals(
                        listOf("Concise", "Safe", "Versatile"), page?.coolestFeatures
                )
            }

            it("should get the bonus features") {
                assertEquals(listOf("Tooling", "Interoperable"), page?.bonusFeatures)
            }
        }
    }

    given("the selector of an element that must be present") {
        val selector = ".global-header-logo"

        on("waiting for the element located by such selector to be present") {
            var itSucceed = true

            try {
                Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                    to("http://kotlinlang.org/")

                    waitFor {
                        presenceOfElementLocated(By.cssSelector(selector))
                    }
                }
            } catch (ignore: TimeoutException) {
                itSucceed = false
            }

            it("should success") {
                assertEquals(true, itSucceed)
            }
        }
    }

    given("the selector of an element that shouldn't be present") {
        val selector = "#wrong.selector"

        on("waiting for the element located by such selector to be present") {
            var itFailed = false

            try {
                Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                    to("http://kotlinlang.org/")

                    waitFor {
                        presenceOfElementLocated(By.cssSelector(selector))
                    }
                }
            } catch (ignore: TimeoutException) {
                itFailed = true
            }

            it("should fail") {
                assertEquals(true, itFailed)
            }
        }
    }

    given("an HtmlUnitDriver with JavaScript enabled") {
        val driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45).apply {
            setJavascriptEnabled(true)
        }

        on("executing a simple `console.log`") {
            Browser.drive(driver = driver) {
                to("http://kotlinlang.org/")

                var itSucceed = true

                try {
                    js.call { "console.log(\"Hello, world!\")" }
                } catch (ignore: ScriptException) {
                    itSucceed = false
                }

                it("should success") {
                    assertEquals(true, itSucceed)
                }
            }

            on("executing an invalid JavaScript code") {
                Browser.drive(driver = driver) {
                    to("http://kotlinlang.org/")
                    var itFailed = false

                    try {
                        js.call { "an obvious bad JavaScript code" }
                    } catch (ignore: ScriptException) {
                        itFailed = true
                    }

                    it("should fail") {
                        assertEquals(true, itFailed)
                    }
                }
            }

            on("executing JavaScript code with arguments with `call`") {
                Browser.drive(driver = driver) {
                    to("http://kotlinlang.org/")
                    val arguments = js.call(1, 2) {
                        "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
                    }

                    it("should return the arguments as is") {
                        assertEquals("Arguments: 1, 2", arguments)
                    }
                }
            }
            on("executing JavaScript code with arguments with `execute`") {
                Browser.drive(driver = driver) {
                    to("http://kotlinlang.org/")
                    val arguments = js.execute(true, false) {
                        "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
                    }

                    it("should return the arguments as is") {
                        assertEquals("Arguments: true, false", arguments)
                    }
                }
            }

            on("executing JavaScript code with arguments with `run`") {
                Browser.drive(driver = driver) {
                    to("http://kotlinlang.org/")
                    val arguments = js.run("a", "b") {
                        "return 'Arguments: ' + arguments[0] + ', ' + arguments[1];"
                    }

                    it("should return the arguments as is") {
                        assertEquals("Arguments: a, b", arguments)
                    }
                }
            }

            on("setting a global variable") {
                Browser.drive(driver = driver) {
                    to("http://kotlinlang.org/")
                    js["myGlobal"] = "global variable"

                    val globalVariableValue = js.execute { "return window.myGlobal;" }

                    it("should return the variable's value as is") {
                        assertEquals("global variable", globalVariableValue)
                    }
                }
            }

            on("setting a global variable") {
                Browser.drive(driver = driver) {
                    to("http://kotlinlang.org/")
                    js["myGlobal"] = "super global variable"

                    val globalVariableValue = js["myGlobal"]

                    it("should get the variable's value as is") {
                        assertEquals("super global variable", globalVariableValue)
                    }
                }
            }
        }
    }
})
/* ***************************************************************************/
