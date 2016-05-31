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
/* ***************************************************************************/

/* ***************************************************************************/
interface JQueryInterface : SearchContext {
  fun `$`(selector: String, index: Int): WebElement {
    return find(selector, index)
  }

  fun `$`(selector: String, range: IntRange): List<WebElement> {
    return find(selector, range)
  }

  fun `$`(selector: String, vararg indexes: Int): List<WebElement> {
    return find(selector, *indexes)
  }

  fun find(selector: String, index: Int): WebElement {
    return findElements(By.cssSelector(selector))[index]
  }

  fun find(selector: String, range: IntRange): List<WebElement> {
    return findElements(By.cssSelector(selector)).slice(range)
  }

  fun find(selector: String, vararg indexes: Int): List<WebElement> {
    val elements = findElements(By.cssSelector(selector))

    if (indexes.size == 0) {
      return elements
    }

    return elements.slice(indexes.asList())
  }
}
/* ***************************************************************************/
