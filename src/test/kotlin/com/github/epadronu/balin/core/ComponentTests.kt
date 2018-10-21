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
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX_60 as BROWSER_VERSION
/* ***************************************************************************/

/* ***************************************************************************/
val expectedFeatures = mapOf(
    "Concise" to "Drastically reduce the amount of boilerplate code.",
    "Safe" to "Avoid entire classes of errors such as null pointer exceptions.",
    "Interoperable" to "Leverage existing libraries for the JVM, Android, and the browser.",
    "Tool-friendly" to "Choose any Java IDE or build from the command line."
)
/* ***************************************************************************/

/* ***************************************************************************/
class ComponentTests {

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create the no JavaScript-enabled WebDriver`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BROWSER_VERSION) })
    )

    @Test(description = "Model pieces of the page as single and nested components",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun model_pieces_of_the_page_as_single_and_nested_components(driverFactory: () -> WebDriver) {
        // Given a component for the Kotlin's features
        class Feature(page: Page, element: WebElement) : Component(page, element) {

            val title by lazy {
                `$`("h3.feature-title", 0).text
            }

            val description by lazy {
                `$`("p.feature-description", 0).text
            }

            override fun toString(): String {
                return "Feature(title = $title, description = $description)"
            }
        }

        // And a component of the section on features
        class FeaturesSection(page: Page, element: WebElement) : Component(page, element) {

            val title by lazy {
                `$`("h2.section-header", 0).text
            }

            val features by lazy {
                `$`("li.kotlin-feature").component(::Feature)
            }
        }

        // And the Kotlin's website index page
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"

            override val at = at {
                title == "Kotlin Programming Language"
            }

            val featuresSection by lazy {
                `$`("section.kotlin-overview-section._features", 0).component(::FeaturesSection)
            }
        }

        // When I visit the Kotlin's website index page
        Browser.drive(driverFactory) {
            val indexPage = to(::IndexPage)

            // Then the header for the features section must be correct
            assertEquals(indexPage.featuresSection.title, "Why Kotlin?")

            val actualFeatures = indexPage.featuresSection.features.associateBy(
                Feature::title, Feature::description
            )

            // And the features should be correctly described inside said section
            assertEquals(actualFeatures, expectedFeatures)
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Use browser#at in a component to place the browser at a different page`(driverFactory: () -> WebDriver) {
        // Given the Kotlin's reference page
        class ReferencePage(browser: Browser) : Page(browser) {

            override val at = at {
                title == "Reference - Kotlin Programming Language"
            }

            val header by lazy {
                `$`("h1", 0).text
            }
        }

        // And a component for the navigation links
        class NavLinks(page: Page, element: WebElement) : Component(page, element) {

            fun goToLearnPage(): ReferencePage {
                `$`("a", 0).click()

                return browser.at(::ReferencePage)
            }
        }

        // And the Kotlin's website index page
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"

            override val at = at {
                title == "Kotlin Programming Language"
            }

            val navLinks by lazy {
                `$`("div.nav-links", 0).component(::NavLinks)
            }
        }

        // When I visit the Kotlin's website index page
        Browser.drive(driverFactory) {
            val indexPage = to(::IndexPage)

            // And I click on the Learn navigation link
            val referencePage = indexPage.navLinks.goToLearnPage()

            // Then the browser should land on the Reference page
            assertEquals(referencePage.header, "Learn Kotlin")
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Use WebElement#click in a component to place the browser at a different page`(driverFactory: () -> WebDriver) {
        // Given the Kotlin's reference page
        class ReferencePage(browser: Browser) : Page(browser) {

            override val at = at {
                title == "Reference - Kotlin Programming Language"
            }

            val header by lazy {
                `$`("h1", 0).text
            }
        }

        // And a component for the navigation links
        class NavLinks(page: Page, element: WebElement) : Component(page, element) {

            fun goToLearnPage() = `$`("a", 0).click(::ReferencePage)
        }

        // And the Kotlin's website index page
        class IndexPage(browser: Browser) : Page(browser) {

            override val url = "http://kotlinlang.org/"

            override val at = at {
                title == "Kotlin Programming Language"
            }

            val navLinks by lazy {
                `$`("div.nav-links", 0).component(::NavLinks)
            }
        }

        // When I visit the Kotlin's website index page
        Browser.drive(driverFactory) {
            val indexPage = to(::IndexPage)

            // And I click on the Learn navigation link
            val referencePage = indexPage.navLinks.goToLearnPage()

            // Then the browser should land on the Reference page
            assertEquals(referencePage.header, "Learn Kotlin")
        }
    }
}
/* ***************************************************************************/
