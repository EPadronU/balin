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
abstract class Page(val browser: Browser) : JavaScriptSupport by browser,
    SearchContext by browser,
    WaitingSupport by browser {

    companion object {
        @JvmStatic
        fun at(block: Browser.() -> Boolean): Browser.() -> Boolean = block
    }

    open val at: Browser.() -> Boolean = { true }

    open val url: String? = null

    fun <T : Page> WebElement.click(factory: (Browser) -> T): T {
        this.click()

        return browser.at(factory)
    }

    fun <T : Component> WebElement.component(factory: (Page, WebElement) -> T): T = factory(this@Page, this)

    fun <T : Component> List<WebElement>.component(factory: (Page, WebElement) -> T): List<T> = this.map {
        factory(this@Page, it)
    }

    internal fun verifyAt(): Boolean {
        return at(browser)
    }
}
/* ***************************************************************************/
