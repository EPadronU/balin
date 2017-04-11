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
import kotlin.test.assertEquals

import com.gargoylesoftware.htmlunit.BrowserVersion
import org.jetbrains.spek.api.Spek
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import com.github.epadronu.balin.exceptions.PageAtValidationError
import com.github.epadronu.balin.extensions.`$`
import com.github.epadronu.balin.libs.delegatesTo
/* ***************************************************************************/

/* ***************************************************************************/
class PageSpec : Spek({
  given("the Kotlin's website index page") {
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"
    }

    on("visiting such page") {
      var currentBrowserPage: IndexPage? = null
      var currentBrowserUrl: String? = null
      var currentPageTitle: String? = null

      Browser.drive(driver=HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
        currentBrowserPage = to(::IndexPage)
        currentBrowserUrl = currentUrl
        currentPageTitle = title
      }

      it("should change the browser's page to the given one") {
        assertEquals(true, currentBrowserPage is IndexPage)
      }

      it("should change the browser's URL to the one of the given page") {
        assertEquals(currentBrowserPage?.url, currentBrowserUrl)
      }

      it("should get the title of the Kotlin's website index page") {
        assertEquals("Kotlin Programming Language", currentPageTitle)
      }
    }
  }

  given("the Kotlin's website index page with a valid `at` clause") {
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"

      override val at = delegatesTo<Browser, Boolean> {
        title == "Kotlin Programming Language"
      }
    }

    on("visiting such page") {
      var currentBrowserPage: IndexPage? = null
      var currentBrowserUrl: String? = null
      var currentPageTitle: String? = null
      var itSucceed = true

      try {
        Browser.drive(driver=HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
          currentBrowserPage = to(::IndexPage)
          currentBrowserUrl = currentUrl
          currentPageTitle = title
        }
      } catch (ignore: PageAtValidationError) {
        itSucceed = false
      }

      it("should change the browser's page to the given one") {
        assertEquals(true, currentBrowserPage is IndexPage)
      }

      it("should change the browser's URL to the one of the given page") {
        assertEquals(currentBrowserPage?.url, currentBrowserUrl)
      }

      it("should get the title of the Kotlin's website index page") {
        assertEquals("Kotlin Programming Language", currentPageTitle)
      }

      it("should success") {
        assertEquals(true, itSucceed)
      }
    }
  }

  given("the Kotlin's website index page with an invalid `at` clause") {
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"

      override val at = delegatesTo<Browser, Boolean> {
        title == "Wrong title"
      }
    }

    on("visiting such page") {
      var itFailed = false

      try {
        Browser.drive(driver=HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
          to(::IndexPage)
        }
      } catch (ignore: PageAtValidationError) {
        itFailed = true
      }

      it("should fail") {
        assertEquals(true, itFailed)
      }
    }
  }

  given("the Kotlin's website index page with content elements") {
    class IndexPage : Page() {
      override val url = "http://kotlinlang.org/"

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
        `$`("li.kotlin-feature", 0..2).`$`("h3:nth-child(2)", 0, 1, 2).map {
          it.text
        }
      }

      val bonusFeatures by lazy {
        `$`("li.kotlin-feature", 4, 3).`$`("h3:nth-child(2)", 0..1).map {
          it.text
        }
      }
    }

    on("visiting such page and getting the content's elements") {
      var bonusFeatures : List<String>? = null
      var coolestFeatures : List<String>? = null
      var navItems : List<String>? = null
      var tryItBtn : String? = null

      Browser.drive(driver=HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
        to(::IndexPage).apply {
          bonusFeatures = this.bonusFeatures
          coolestFeatures = this.coolestFeatures
          navItems = this.navItems
          tryItBtn = this.tryItBtn
        }
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
})
/* ***************************************************************************/
