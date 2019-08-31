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
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.NoSuchFrameException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert.assertEquals
import org.testng.Assert.assertThrows
import org.testng.Assert.fail
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX_60 as BROWSER_VERSION
/* ***************************************************************************/

/* ***************************************************************************/
class WithFrameTests {

    companion object {
        @JvmStatic
        val pageWithIFramesUrl = WithFrameTests::class.java
            .getResource("/test-pages/page-with-iframes.html")
            .toString()
    }

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create a JavaScript-incapable WebDriver factory`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BROWSER_VERSION) })
    )

    @Test(description = "Validate context switching to and from an IFrame with index",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun validate_context_switching_to_and_from_an_iframe_with_index(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains three IFrames
            to(pageWithIFramesUrl)

            // And I'm in the context of the page
            assertEquals(`$`("h1", 0).text, "Page with IFrames")

            // When I change the driver's context to the second IFrame
            withFrame(1) {
                // Then I should be able to interact with such IFrame
                assertEquals(
                    `$`(".tag-home__item", 0).text.trim(),
                    "The search engine that doesn't track you. Learn More.")
            }

            // And I should return into the context of the page at the end of the `withFrame` method
            assertEquals(`$`("h1", 0).text, "Page with IFrames")
        }
    }

    @Test(description = "Validate context switching to and from an IFrame with ID",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun validate_context_switching_to_and_from_an_iframe_with_id(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains three IFrames
            to(pageWithIFramesUrl)

            // And I'm in the context of the page
            assertEquals(`$`("h1", 0).text, "Page with IFrames")

            // When I change the driver's context to the searx.me IFrame
            withFrame("searx-iframe") {
                // Then I should be able to interact with such IFrame
                assertEquals(`$`("#main-logo", 0).text.trim(), "searx")
            }

            // And I should return into the context of the page at the end of the `withFrame` method
            assertEquals(`$`("h1", 0).text, "Page with IFrames")
        }
    }

    @Test(description = "Validate context switching to and from an IFrame with WebElement",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun validate_context_switching_to_and_from_an_iframe_with_web_element(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains three IFrames
            to(pageWithIFramesUrl)

            // And I'm in the context of the page
            assertEquals(`$`("h1", 0).text, "Page with IFrames")

            // When I change the driver's context to the first IFrame
            withFrame(`$`("iframe", 0)) {
                // Then I should be able to interact with such IFrame
                assertEquals(`$`(".overview-header", 0).text, "Try Kotlin")
            }

            // And I should return into the context of the page at the end of the `withFrame` method
            assertEquals(`$`("h1", 0).text, "Page with IFrames")
        }
    }

    @Test(description = "Validate context switching to and from an IFrame with index and pages",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun validate_context_switching_to_and_from_an_iframe_with_index_and_pages(driverFactory: () -> WebDriver) {
        // Given a Page Object for the page under test, which contains three IFrames
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = pageWithIFramesUrl

            override val at = at {
                title == "Page with IFrames"
            }

            val headerText
                get() = `$`("h1", 0).text
        }

        // And a Page Object for the DuckDuckGo IFrame
        class DuckDuckGoHomePage(browser: Browser) : Page(browser) {

            override val at = at {
                `$`(".cw--c > div > a", 0).text.trim() == "About DuckDuckGo"
            }

            val homeFooterText
                get() = `$`(".tag-home__item", 0).text.trim()
        }

        Browser.drive(driverFactory) {
            // And I navigate to the page under test
            val indexPage = to(::IndexPage)

            // And I'm in the context of such page
            assertEquals(indexPage.headerText, "Page with IFrames")

            // When I change the driver's context to DuckDuckGo IFrame
            withFrame<DuckDuckGoHomePage>(1) {
                // Then I should be able to interact with such IFrame via its Page Object
                assertEquals(homeFooterText, "The search engine that doesn't track you. Learn More.")
            }

            // And I should return into the context of the page under test at the end of the `withFrame` method
            assertEquals(indexPage.headerText, "Page with IFrames")
        }
    }

    @Test(description = "Validate context switching to and from an IFrame with ID and pages",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun validate_context_switching_to_and_from_an_iframe_with_id_and_pages(driverFactory: () -> WebDriver) {
        // Given a Page Object for the page under test, which contains three IFrames
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = pageWithIFramesUrl

            override val at = at {
                title == "Page with IFrames"
            }

            val headerText
                get() = `$`("h1", 0).text
        }

        // And a Page Object for the searx.me IFrame
        class SearxHomePage(browser: Browser) : Page(browser) {

            override val at = at {
                `$`(".instance > a", 0).text.trim() == "searx.me"
            }

            val logoImageText
                get() = `$`("#main-logo", 0).text
        }

        Browser.drive(driverFactory) {
            // And I navigate to the page under test
            val indexPage = to(::IndexPage)

            // And I'm in the context of such page
            assertEquals(indexPage.headerText, "Page with IFrames")

            // When I change the driver's context to searx.me IFrame
            withFrame<SearxHomePage>("searx-iframe") {
                // Then I should be able to interact with such IFrame via its Page Object
                assertEquals(logoImageText, "searx")
            }

            // And I should return into the context of the page under test at the end of the `withFrame` method
            assertEquals(indexPage.headerText, "Page with IFrames")
        }
    }

    @Test(description = "Validate context switching to and from an IFrame with WebElement and pages",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun validate_context_switching_to_and_from_an_iframe_with_web_element_and_pages(driverFactory: () -> WebDriver) {
        // Given a Page Object for the page under test, which contains three IFrames
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = pageWithIFramesUrl

            override val at = at {
                title == "Page with IFrames"
            }

            val headerText
                get() = `$`("h1", 0).text
        }

        // And a Page Object for the KotlinLang IFrame
        class KotlinLangIndexPage(browser: Browser) : Page(browser) {

            override val at = at {
                `$`("a.global-header-logo", 0).text == "Kotlin"
            }

            val tryKotlinHeaderText
                get() = `$`(".overview-header", 0).text
        }

        Browser.drive(driverFactory) {
            // And I navigate to the page under test
            val indexPage = to(::IndexPage)

            // And I'm in the context of such page
            assertEquals(indexPage.headerText, "Page with IFrames")

            // When I change the driver's context to KotlinLang IFrame
            withFrame<KotlinLangIndexPage>(`$`("iframe", 0)) {
                // Then I should be able to interact with such IFrame via its Page Object
                assertEquals(tryKotlinHeaderText, "Try Kotlin")
            }

            // And I should return into the context of the page under test at the end of the `withFrame` method
            assertEquals(indexPage.headerText, "Page with IFrames")
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Frame switching should fail when an invalid IFrame index is provided`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains three IFrames
            to(pageWithIFramesUrl)

            // When I change the driver's context via an invalid index
            // Then a failure should be communicated
            assertThrows(NoSuchFrameException::class.java) {
                withFrame(4) {
                    fail("This shouldn't be reached")
                }
            }

            // And I should return into the context of the page at the end of the `withFrame` method
            assertEquals(`$`("h1", 0).text, "Page with IFrames")
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Frame switching should fail when an invalid IFrame ID is provided`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains three IFrames
            to(pageWithIFramesUrl)

            // When I change the driver's context via an invalid ID
            // Then a failure should be communicated
            assertThrows(NoSuchFrameException::class.java) {
                withFrame("invalid-iframe") {
                    fail("This shouldn't be reached")
                }
            }

            // And I should return into the context of the page at the end of the `withFrame` method
            assertEquals(`$`("h1", 0).text, "Page with IFrames")
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Frame switching should fail when a WebElement other than an IFrame is provided`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains three IFrames
            to(pageWithIFramesUrl)

            // When I change the driver's context via a WebElement other than an IFrame
            // Then a failure should be communicated
            assertThrows(NoSuchFrameException::class.java) {
                withFrame(`$`("p", 0)) {
                    fail("This shouldn't be reached")
                }
            }

            // And I should return into the context of the page at the end of the `withFrame` method
            assertEquals(`$`("h1", 0).text, "Page with IFrames")
        }
    }
}
/* ***************************************************************************/
