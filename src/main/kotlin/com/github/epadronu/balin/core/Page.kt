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
/**
 * This class is the corner stone for Balin's implementation of the
 * _Page Object Design Pattern_. All classes that model a Web page/view most
 * extend this one.
 *
 * @sample com.github.epadronu.balin.core.PageTests.model_a_page_into_a_page_object_navigate_and_interact_with
 *
 * @param browser the browser used by the page in order to interact with the underlying web content.
 * @constructor Create a new instance with the given browser as its bridge with the web content the page care about.
 */
abstract class Page(val browser: Browser) : ClickAndNavigateSupport,
    ComponentMappingSupport,
    JavaScriptSupport by browser,
    SearchContext by browser,
    WaitingSupport by browser {

    companion object {
        /**
         * This method eases the definition of a page's _implicit at verification_.
         *
         * @sample com.github.epadronu.balin.core.PageTests.model_a_page_into_a_page_object_navigate_and_interact_with
         *
         * @param block context within which you can interact with the browser.
         * @return The [block] unchanged.
         */
        @JvmStatic
        fun at(block: Browser.() -> Any): Browser.() -> Any = block
    }

    /**
     * Defines an optional _implicit verification_ to be checked as soon as the
     * browser navigates to the page.
     *
     * Useful for performing early failure.
     *
     * @sample com.github.epadronu.balin.core.PageTests.model_a_page_into_a_page_object_navigate_and_interact_with
     */
    open val at: Browser.() -> Any = { true }

    /**
     * Defines an optional URL, which will be used when invoking
     * [Browser.to] with a page factory.
     *
     * @sample com.github.epadronu.balin.core.PageTests.model_a_page_into_a_page_object_navigate_and_interact_with
     */
    open val url: String? = null

    /**
     * Click on an element and tells the browser it will navigate to the given
     * page as consequence of such action.
     *
     * @sample com.github.epadronu.balin.core.PageTests.use_WebElement_click_in_a_page_to_place_the_browser_at_a_different_page
     *
     * @receiver the [WebElement][org.openqa.selenium.WebElement] to be clicked on.
     * @param factory provides an instance of the page given the driver being used by the browser.
     * @Returns An instance of the page the browser will navigate to.
     * @throws PageImplicitAtVerificationException if the page has an _implicit at verification_ which have failed.
     */
    override fun <T : Page> WebElement.click(factory: (Browser) -> T): T {
        this.click()

        return browser.at(factory)
    }

    override fun <T : Component> WebElement.component(factory: (Page, WebElement) -> T): T = factory(this@Page, this)

    override fun <T : Component> List<WebElement>.component(factory: (Page, WebElement) -> T): List<T> = this.map {
        factory(this@Page, it)
    }

    /**
     * Evaluate the page's _implicit at verification_.
     *
     * @return true if the verification passed, false otherwise.
     */
    internal fun verifyAt(): Boolean = when (val result = at(browser)) {
        is Boolean -> result
        is Unit -> true
        else -> throw Error("Expressions of type `${result.javaClass.canonicalName}` are not allowed.")
    }
}
/* ***************************************************************************/
