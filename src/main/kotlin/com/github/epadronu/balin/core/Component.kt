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
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
/* ***************************************************************************/

/* ***************************************************************************/
abstract class Component(private val page: Page, element: WebElement) : JavaScriptSupport by page,
    SearchContext by element,
    WaitingSupport by page {

    val browser = page.browser

    fun <T : Page> WebElement.click(factory: (Browser) -> T): T {
        this.click()

        return browser.at(factory)
    }

    fun <T : Component> WebElement.component(factory: (Page, WebElement) -> T): T = factory(page, this)

    fun <T : Component> List<WebElement>.component(factory: (Page, WebElement) -> T): List<T> = this.map {
        factory(page, it)
    }
}
/* ***************************************************************************/
