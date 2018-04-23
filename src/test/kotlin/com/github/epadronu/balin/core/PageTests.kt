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
import com.github.epadronu.balin.exceptions.PageAtValidationError
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class PageTests {

  private lateinit var driver: WebDriver

  @BeforeMethod
  fun `Create the Web Driver`() {
    driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52)
  }

  @Test
  fun `Model a page into a Page Object and navigate to it`() {
    // Given the Kotlin's website index page
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"
    }

    // When I visit such page
    var currentBrowserPage: IndexPage? = null
    lateinit var currentBrowserUrl: String
    lateinit var currentPageTitle: String

    Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52)) {
      currentBrowserPage = to(::IndexPage)
      currentBrowserUrl = currentUrl
      currentPageTitle = title
    }

    // Then I should change the browser's page to the given one
    assertEquals(true, currentBrowserPage is IndexPage)

    // And I should change the browser's URL to the one of the given page
    assertEquals(currentBrowserPage?.url, currentBrowserUrl)

    // And I should get the title of the Kotlin's website index page
    assertEquals("Kotlin Programming Language", currentPageTitle)
  }

  @Test
  fun `Model a page into a Page Object with a valid at clause`() {
    // Given the Kotlin's website index page with a valid `at` clause
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"

      override val at = at {
        title == "Kotlin Programming Language"
      }
    }

    // When I visit such page
    var itSucceed = true

    try {
      Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52)) {
        to(::IndexPage)
      }
    } catch (ignore: PageAtValidationError) {
      itSucceed = false
    }

    // Then the navigation should be successful
    assertTrue(itSucceed)
  }

  @Test
  fun `Model a page into a Page Object with a invalid at clause`() {
    // Given the Kotlin's website index page with an invalid `at` clause
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"

      override val at = at {
        title == "Wrong title"
      }
    }

    // When I visit such page
    var itFailed = false

    try {
      Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52)) {
        to(::IndexPage)
      }
    } catch (ignore: PageAtValidationError) {
      itFailed = true
    }

    // Then the navigation should be a failure
    assertTrue(itFailed)
  }

  @Test
  fun `Model a page into a Page Object navigate and interact with`() {
    // Given the Kotlin's website index page with content elements
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"

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
        `$`("li.kotlin-feature", 0, 1, 2, 3).`$`("h3:nth-child(2)", 0..3).map {
          it.text
        }
      }
    }

    // When I visit such page and get the content's elements
    lateinit var features: List<String>
    lateinit var navItems: List<String>
    lateinit var tryItBtn: String

    Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_52)) {
      to(::IndexPage).apply {
        features = this.features
        navItems = this.navItems
        tryItBtn = this.tryItBtn
      }
    }

    // Then I should get the navigation items
    assertEquals(listOf("Learn", "Community", "Try Online"), navItems)

    // And I should get the try-it button
    assertEquals("Try Kotlin", tryItBtn)

    // And I should get the coolest features
    assertEquals(listOf("Concise", "Safe", "Interoperable", "Tool-friendly"), features)
  }

  @Test
  fun `Use WebElement#click in a page to place the browser at a different page`() {
    // Given the Kotlin's reference page
    class ReferencePage : Page() {
      override val at = at {
        title == "Reference - Kotlin Programming Language"
      }

      val header by lazy {
        `$`("h1", 0).text
      }
    }

    // And the Kotlin's website index page
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"

      override val at = at {
        title == "Kotlin Programming Language"
      }

      fun goToLearnPage() = `$`("div.nav-links a", 0).click(::ReferencePage)
    }

    // When I visit the Kotlin's website index page
    Browser.drive(driver) {
      val indexPage = to(::IndexPage)

      // And I click on the Learn navigation link
      val referencePage = indexPage.goToLearnPage()

      // Then the browser should land on the Reference page
      assertEquals(referencePage.header, "Reference")
    }
  }
}
/* ***************************************************************************/
