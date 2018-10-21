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
import org.openqa.selenium.WebElement
/* ***************************************************************************/

/* ***************************************************************************/
/**
 * This interface defines methods to easily map a
 * [WebElement][org.openqa.selenium.WebElement]into a [Component].
 */
interface ComponentMappingSupport {

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
    fun <T : Component> WebElement.component(factory: (Page, WebElement) -> T): T

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
    fun <T : Component> List<WebElement>.component(factory: (Page, WebElement) -> T): List<T>
}
/* ***************************************************************************/
