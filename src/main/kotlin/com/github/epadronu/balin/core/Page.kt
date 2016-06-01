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
import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
/* ***************************************************************************/

/* ***************************************************************************/
abstract class Page(var browser: Browser = DriverlessBrowser()) : SearchContext, WaitingSupport {
  abstract val at: Browser.() -> Boolean

  abstract val url: String?

  override fun findElement(by: By): WebElement {
    return browser.findElement(by)
  }

  override fun findElements(by: By): List<WebElement> {
    return browser.findElements(by)
  }

  override fun <T> waitFor(timeOutInSeconds: Long, sleepInMillis: Long, isTrue: () -> ExpectedCondition<T>): T {
    return browser.waitFor(timeOutInSeconds, sleepInMillis, isTrue)
  }

  fun verifyAt(): Boolean {
    return at(browser)
  }
}
/* ***************************************************************************/
