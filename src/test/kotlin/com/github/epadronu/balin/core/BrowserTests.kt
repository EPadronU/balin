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
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.By
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class BrowserTests {

  private lateinit var driver: WebDriver

  @BeforeMethod
  fun `Create the Web Driver`() {
    driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52)
  }

  @Test
  fun `Perform a simple web navigation`() {
    // Given the Kotlin's website index page URL
    val indexPageUrl = "http://kotlinlang.org/"

    // When I visit such URL
    lateinit var currentBrowserUrl: String
    lateinit var currentPageTitle: String

    Browser.drive(driver) {
      currentBrowserUrl = to(indexPageUrl)
      currentPageTitle = title
    }

    // Then I should change the browser's URL to the given one
    assertEquals(indexPageUrl, currentBrowserUrl)

    // And I should get the title of the Kotlin's website index page
    assertEquals("Kotlin Programming Language", currentPageTitle)
  }

  @Test
  fun `Find some basic elements in the page`() {
    // Given the Kotlin's website index page URL
    val indexPageUrl = "http://kotlinlang.org/"

    // And a couple of CSS selectors
    val featuresSelector = "li.kotlin-feature > h3:nth-child(2)"
    val navItemsSelector = "a.nav-item"
    val tryItBtnSelector = ".get-kotlin-button"

    // When I visit such URL and get the elements for said selectors
    lateinit var features: List<String>
    lateinit var navItems: List<String>
    lateinit var tryItBtn: String

    Browser.drive(driver) {
      to(indexPageUrl)

      features = `$`(featuresSelector).map { it.text }
      navItems = `$`(navItemsSelector).map { it.text }
      tryItBtn = `$`(tryItBtnSelector, 0).text
    }

    // Then I should get the navigation items
    assertEquals(listOf("Learn", "Community", "Try Online"), navItems)

    // And I should get the try-it button
    assertEquals("Try Kotlin", tryItBtn)

    // And I should get the features
    assertEquals(listOf("Concise", "Safe", "Interoperable", "Tool-friendly"), features)
  }

  @Test
  fun `Model a page into a Page Object and interact with it via the at method`() {
    // Given the Kotlin's website index page with content elements and no URL
    class IndexPage : Page() {
      override val url: String? = null

      override val at = at {
        title == "Kotlin Programming Language"
      }

      val navItems by lazy {
        `$`("a.nav-item").map { it.text }
      }

      val tryItBtn by lazy {
        `$`(".get-kotlin-button", 0).text
      }

      val features by lazy {
        `$`("li.kotlin-feature").`$`("h3:nth-child(2)").map { it.text }
      }
    }

    // When I visit the index page URL and set the browser's page with `at`
    var page: IndexPage? = null

    Browser.drive(driver) {
      to("http://kotlinlang.org/")

      page = at(::IndexPage).apply {
        // In order to execute the lazy evaluation and cache the results
        features; navItems; tryItBtn
      }
    }

    // The I should change the browser's page to the given one
    assertEquals(true, page is IndexPage)

    // And I should get the navigation items
    assertEquals(listOf("Learn", "Community", "Try Online"), page?.navItems)

    // And I should get the try-it button
    assertEquals("Try Kotlin", page?.tryItBtn)

    // And I should get the coolest features
    assertEquals(
      listOf("Concise", "Safe", "Interoperable", "Tool-friendly"), page?.features
    )
  }

  @Test
  fun `Wait for the presence of an element that should be there`() {
    // Given the selector of an element that should be present
    val locator = By.cssSelector(".global-header-logo")

    // When I wait for the element located by such selector to be present
    var itSucceed = true

    try {
      Browser.drive(driver) {
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

  @Test
  fun `Wait for the presence of an element that shouldn't be there`() {
    // Given the selector of an element that shouldn't be present
    val locator = By.cssSelector("#wrong.selector")

    // When I wait for the element located by such selector to be present
    var itFailed = false

    try {
      Browser.drive(driver) {
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
}
/* ***************************************************************************/
