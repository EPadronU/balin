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
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
/* ***************************************************************************/

/* ***************************************************************************/
class DriverlessBrowser : Browser {
  override fun getWindowHandles(): MutableSet<String>? {
    throw UnsupportedOperationException()
  }

  override fun findElement(by: By?): WebElement? {
    throw UnsupportedOperationException()
  }

  override fun getWindowHandle(): String? {
    throw UnsupportedOperationException()
  }

  override fun getPageSource(): String? {
    throw UnsupportedOperationException()
  }

  override fun navigate(): WebDriver.Navigation? {
    throw UnsupportedOperationException()
  }

  override fun manage(): WebDriver.Options? {
    throw UnsupportedOperationException()
  }

  override fun getCurrentUrl(): String? {
    throw UnsupportedOperationException()
  }

  override fun getTitle(): String? {
    throw UnsupportedOperationException()
  }

  override fun get(url: String?) {
    throw UnsupportedOperationException()
  }

  override fun switchTo(): WebDriver.TargetLocator? {
    throw UnsupportedOperationException()
  }

  override fun close() {
    throw UnsupportedOperationException()
  }

  override fun quit() {
    throw UnsupportedOperationException()
  }

  override fun findElements(by: By?): MutableList<WebElement>? {
    throw UnsupportedOperationException()
  }

  override fun <T> waitFor(timeOutInSeconds: Long, sleepInMillis: Long, isTrue: () -> ExpectedCondition<T>): T {
    throw UnsupportedOperationException()
  }
}
/* ***************************************************************************/
