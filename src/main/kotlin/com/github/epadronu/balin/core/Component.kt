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
 * @param rootElement the component's root element.
 * @constructor Create a new component, given the page it's linked to and its root element.
 */
abstract class Component(val page: Page, val rootElement: WebElement) : ClickAndNavigateSupport by page,
    ComponentMappingSupport by page,
    JavaScriptSupport by page,
    SearchContext by rootElement,
    WaitingSupport by page {

    /**
     * The browser used by the component in order to interact with the
     * underlying web content.
     */
    val browser: Browser = page.browser
}
/* ***************************************************************************/
