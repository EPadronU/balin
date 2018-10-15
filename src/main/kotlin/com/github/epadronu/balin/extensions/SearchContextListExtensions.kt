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
package com.github.epadronu.balin.extensions
/* ***************************************************************************/

/* ***************************************************************************/
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
/* ***************************************************************************/

/* ***************************************************************************/
/**
 * Find the nth element that can be located within the current context by the
 * given CSS selector.
 *
 * This is an alternative to the `find` method.
 *
 * @param selector the CSS selector to be used for locating the element.
 * @param index the index of the element to be returned.
 * @return The nth matching element within the current context.
 * @throws java.lang.IndexOutOfBoundsException for an illegal index value.
 * @sample com.github.epadronu.balin.core.BrowserTests.find_some_basic_elements_in_the_page
 */
fun List<SearchContext>.`$`(selector: String, index: Int): WebElement = find(selector, index)

/**
 * Find all the elements that can be located by the given CSS selector within
 * the current context, restricted by the specified range.
 *
 * This is an alternative to the `find` method.
 *
 * @param selector the CSS selector to be used for locating the elements.
 * @param range specify the indices of the elements to be returned.
 * @return The matching elements within the current context, restricted by the specified range.
 * @throws java.lang.IndexOutOfBoundsException for illegal index values within the range.
 * @sample com.github.epadronu.balin.core.BrowserTests.find_some_basic_elements_in_the_page
 */
fun List<SearchContext>.`$`(selector: String, range: IntRange): List<WebElement> = find(selector, range)

/**
 * Find all the elements that can be located by the given CSS selector within
 * the current context, restricted by the specified indices. (If no index is
 * provided, then all matching elements will be returned.)
 *
 * This is an alternative to the `find` method.
 *
 * @param selector the CSS selector to be used for locating the elements.
 * @param indices the indices of the elements to be returned.
 * @return The matching elements within the current context restricted by the specified indices. (Or all matching elements if no index is provided.)
 * @throws java.lang.IndexOutOfBoundsException for illegal index values.
 * @sample com.github.epadronu.balin.core.BrowserTests.find_some_basic_elements_in_the_page
 */
fun List<SearchContext>.`$`(selector: String, vararg indices: Int): List<WebElement> = find(selector, *indices)

/**
 * Find the nth element that can be located within the current context by the
 * given CSS selector.
 *
 * @param selector the CSS selector to be used for locating the element.
 * @param index the index of the element to be returned.
 * @return The nth matching element within the current context.
 * @throws java.lang.IndexOutOfBoundsException for illegal index values.
 * @sample com.github.epadronu.balin.core.BrowserTests.find_some_basic_elements_in_the_page
 */
fun List<SearchContext>.find(selector: String, index: Int): WebElement = this.map {
    it.find(selector)
}.flatten()[index]

/**
 * Find all the elements that can be located by the given CSS selector within
 * the current context, restricted by the specified range.
 *
 * @param selector the CSS selector to be used for locating the elements.
 * @param range specify the indices of the elements to be returned.
 * @return The matching elements within the current context, restricted by the specified range.
 * @throws java.lang.IndexOutOfBoundsException for illegal index values within the range.
 * @sample com.github.epadronu.balin.core.BrowserTests.find_some_basic_elements_in_the_page
 */
fun List<SearchContext>.find(selector: String, range: IntRange): List<WebElement> = this.map {
    it.find(selector)
}.flatten().slice(range)

/**
 * Find all the elements that can be located by the given CSS selector within
 * the current context, restricted by the specified indices. (If no index is
 * provided, then all matching elements will be returned.)
 *
 * @param selector the CSS selector to be used for locating the elements.
 * @param indices the indices of the elements to be returned.
 * @return The matching elements within the current context, restricted by the specified indices. (Or all matching elements if no index is provided.)
 * @throws java.lang.IndexOutOfBoundsException for illegal index values.
 * @sample com.github.epadronu.balin.core.BrowserTests.find_some_basic_elements_in_the_page
 */
fun List<SearchContext>.find(selector: String, vararg indices: Int): List<WebElement> {
    val elements = this.map { it.find(selector) }.flatten()

    if (indices.isEmpty()) {
        return elements
    }

    return elements.slice(indices.asList())
}
/* ***************************************************************************/
