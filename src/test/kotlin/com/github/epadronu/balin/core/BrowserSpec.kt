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
/* ***************************************************************************/

/* ***************************************************************************/
class BrowserSpec : Spek({
  given("the Kotlin's website index page URL") {
    val indexPageUrl = "http://kotlinlang.org/"

    on("visiting such URL") {
      var currentBrowserUrl: String? = null
      var currentPageTitle: String? = null

      Browser.drive(driver=HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
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
})
/* ***************************************************************************/
