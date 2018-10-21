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
 * A component is a reusable piece of functionality that can be shared among
 * several pages, and which interaction can be performed independently of other
 * pieces in the web page.
 *
 * @sample com.github.epadronu.balin.core.ComponentTests.model_pieces_of_the_page_as_single_and_nested_components
 *
 * @param page the page the component is linked to.
 * @param element the component's root element.
 * @constructor Create a new component, given the page it's linked to and its root element.
 */
abstract class Component(private val page: Page, element: WebElement) : JavaScriptSupport by page,
    SearchContext by element,
    WaitingSupport by page {

    /**
     * The browser used by the component in order to interact with the
     * underground web content.
     */
    val browser = page.browser

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
    fun <T : Page> WebElement.click(factory: (Browser) -> T): T {
        this.click()

        return browser.at(factory)
    }

    /**
     * Create a new component with the given
     * [WebElement][org.openqa.selenium.WebElement] as its root element.
     *
     * Depending on how the component is designed, the interactions with the
     * underground web content may be performed relatively to the component's
     * root element.
     *
     * @sample com.github.epadronu.balin.core.ComponentTests.model_pieces_of_the_page_as_single_and_nested_components
     *
     * @receiver The component's root element.
     * @param factory provides an instance of the component, given the page it's linked to and its root element.
     * @return An instance of the desired component.
     */
    fun <T : Component> WebElement.component(factory: (Page, WebElement) -> T): T = factory(page, this)

    /**
     * Map the given collection of [WebElement][org.openqa.selenium.WebElement]
     * into a collection of [com.github.epadronu.balin.core.Component].
     *
     * @sample com.github.epadronu.balin.core.ComponentTests.model_pieces_of_the_page_as_single_and_nested_components
     * @see WebElement.component
     *
     * @receiver The collection to be mapped.
     * @param factory provides an instance of the component, given the page it's linked to and its root element.
     * @return A collection of [com.github.epadronu.balin.core.Component].
     */
    fun <T : Component> List<WebElement>.component(factory: (Page, WebElement) -> T): List<T> = this.map {
        factory(page, it)
    }
}
/* ***************************************************************************/
