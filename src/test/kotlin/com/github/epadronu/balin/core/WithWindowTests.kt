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
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert.assertEquals
import org.testng.Assert.assertThrows
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX_60 as BROWSER_VERSION
/* ***************************************************************************/

/* ***************************************************************************/
class WithWindowTests {

    companion object {
        @JvmStatic
        val pageWithWindowsUrl = WithWindowTests::class.java
            .getResource("/test-pages/page-with-windows.html")
            .toString()
    }

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create a JavaScript-incapable WebDriver factory`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BROWSER_VERSION) })
    )

    @Test(description = "Validate context switching to and from a window",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun validate_context_switching_to_and_from_a_window(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains links that open windows
            to(pageWithWindowsUrl)

            // And I'm in the context of the original window
            assertEquals(`$`("h1", 0).text, "Page with links that open windows")

            // And I open a new window by clicking on a link
            `$`("#ddg-page", 0).click()

            // When I change the driver's context to the just opened window
            withWindow(windowHandles.toSet().minus(windowHandle).first()) {
                // Then I should be able to interact with such window
                assertEquals(
                    `$`(".tag-home__item", 0).text.trim(),
                    "The search engine that doesn't track you. Learn More.")
            }

            // And I should return into the context of the original window
            assertEquals(`$`("h1", 0).text, "Page with links that open windows")

            // And only one window should be opened
            assertEquals(windowHandles.size, 1)
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Validate automatically context switching to and from the last opened window`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains links that open windows
            to(pageWithWindowsUrl)

            // And I'm in the context of the original window
            assertEquals(`$`("h1", 0).text, "Page with links that open windows")

            // And I open a new window by clicking on a link
            `$`("#ddg-page", 0).click()

            // When I change the driver's context to the last opened window
            withWindow {
                // Then I should be able to interact with such window
                assertEquals(
                    `$`(".tag-home__item", 0).text.trim(),
                    "The search engine that doesn't track you. Learn More.")
            }

            // And I should return into the context of the original window
            assertEquals(`$`("h1", 0).text, "Page with links that open windows")

            // And only two window should be opened
            assertEquals(windowHandles.size, 1)
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Validate automatically context switching to and from a window when are none opened fails`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains links that open windows
            to(pageWithWindowsUrl)

            // And I'm in the context of the original window
            assertEquals(`$`("h1", 0).text, "Page with links that open windows")

            // When I try to change the driver's context to another window
            // Then an exception should be thrown
            assertThrows(NoSuchWindowException::class.java) {
                withWindow { }
            }
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Validate automatically context switching to and from a window when several are opened fails`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which contains links that open windows
            to(pageWithWindowsUrl)

            // And I'm in the context of the original window
            assertEquals(`$`("h1", 0).text, "Page with links that open windows")

            // And I open a new window by clicking on a link
            `$`("#ddg-page", 0).click()

            // And I open another window by clicking on a link
            `$`("#ddg-page", 0).click()

            // When I try to change the driver's context to another window
            // Then an exception should be thrown
            assertThrows(NoSuchWindowException::class.java) {
                withWindow { }
            }
        }
    }
}
/* ***************************************************************************/
