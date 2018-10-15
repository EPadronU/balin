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
import com.github.epadronu.balin.exceptions.MissingPageUrlException
import com.github.epadronu.balin.exceptions.PageImplicitAtVerificationException
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert.assertEquals
import org.testng.Assert.expectThrows
import org.testng.Assert.fail
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class PageTests {

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create the no JavaScript-enabled WebDriver`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BrowserVersion.FIREFOX_52) })
    )

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Model a page into a Page Object and navigate to it`(driverFactory: () -> WebDriver) {
        // Given the Kotlin's website index page
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"
        }

        // When I visit such page
        var currentBrowserPage: IndexPage? = null
        lateinit var currentBrowserUrl: String
        lateinit var currentPageTitle: String

        Browser.drive(driverFactory) {
            currentBrowserPage = to(::IndexPage)
            currentBrowserUrl = currentUrl
            currentPageTitle = title
        }

        // Then I should change the browser's page to the given one
        assertEquals(currentBrowserPage is IndexPage, true)

        // And I should change the browser's URL to the one of the given page
        assertEquals(currentBrowserUrl, currentBrowserPage?.url)

        // And I should get the title of the Kotlin's website index page
        assertEquals(currentPageTitle, "Kotlin Programming Language")
    }

    @Test(description = "Model a page into a Page Object with no URL and try to navigate to it",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun model_a_page_into_a_page_object_with_no_url_and_try_to_navigate_to_it(driverFactory: () -> WebDriver) {
        // Given a page with no URL
        class TestPage(browser: Browser) : Page(browser)

        // When I visit such page
        Browser.drive(driverFactory) {
            // Then MissingPageUrlException should be throw
            expectThrows(MissingPageUrlException::class.java) {
                to(::TestPage)
            }
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Model a page into a Page Object with a valid at clause`(driverFactory: () -> WebDriver) {
        // Given the Kotlin's website index page with a valid `at` clause
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"

            override val at = at {
                title == "Kotlin Programming Language"
            }
        }

        try {
            // When I visit such page
            Browser.drive(driverFactory) {
                to(::IndexPage)
            }
        } catch (ignore: PageImplicitAtVerificationException) {
            // Then the navigation should be successful
            fail("An unexpected exception was thrown")
        }
    }

    @Test(description = "Model a page into a Page Object with a invalid at clause",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun model_a_page_into_a_page_object_with_a_invalid_at_clause(driverFactory: () -> WebDriver) {
        // Given the Kotlin's website index page with an invalid `at` clause
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"

            override val at = at {
                title == "Wrong title"
            }
        }

        // When I visit such page
        Browser.drive(driverFactory) {
            // Then the navigation should be a failure
            expectThrows(PageImplicitAtVerificationException::class.java) {
                to(::IndexPage)
            }
        }
    }

    @Test(description = "Model a page into a Page Object navigate and interact with",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun model_a_page_into_a_page_object_navigate_and_interact_with(driverFactory: () -> WebDriver) {
        // Given the Kotlin's website index page with content elements
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"

            override val at = at {
                title == "Kotlin Programming Language"
            }

            val navItems by lazy {
                `$`("a.nav-item", 0..2).map { it.text }
            }

            val tryItBtn by lazy {
                `$`(".try-button", 0).text
            }

            val features by lazy {
                `$`("li.kotlin-feature", 3, 2, 1, 0).`$`("h3:nth-child(2)", 0..3).map {
                    it.text
                }
            }
        }

        // When I visit such page and get the content's elements
        Browser.drive(driverFactory) {
            to(::IndexPage).run {
                // Then I should get the navigation items
                assertEquals(navItems, listOf("Learn", "Community", "Try Online"))

                // And I should get the try-it button
                assertEquals(tryItBtn, "Try online")

                // And I should get the coolest features
                assertEquals(features, listOf("Concise", "Safe", "Interoperable", "Tool-friendly").reversed())
            }
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Use WebElement#click in a page to place the browser at a different page`(driverFactory: () -> WebDriver) {
        // Given the Kotlin's reference page
        class ReferencePage(browser: Browser) : Page(browser) {

            override val at = at {
                title == "Reference - Kotlin Programming Language"
            }

            val header by lazy {
                `$`("h1", 0).text
            }
        }

        // And the Kotlin's website index page
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"

            override val at = at {
                title == "Kotlin Programming Language"
            }

            fun goToLearnPage() = `$`("div.nav-links a", 0).click(::ReferencePage)
        }

        // When I visit the Kotlin's website index page
        Browser.drive(driverFactory) {
            val indexPage = to(::IndexPage)

            // And I click on the Learn navigation link
            val referencePage = indexPage.goToLearnPage()

            // Then the browser should land on the Reference page
            assertEquals(referencePage.header, "Learn Kotlin")
        }
    }
}
/* ***************************************************************************/
