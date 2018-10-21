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
 * This interface defines a method to click on an element and tell the browser
 * it will navigate to a different page as consequence of such action.
 */
interface ClickAndNavigateSupport {

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
    fun <T : Page> WebElement.click(factory: (Browser) -> T): T
}
/* ***************************************************************************/
