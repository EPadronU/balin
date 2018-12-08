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
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNull
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX_60 as BROWSER_VERSION
/* ***************************************************************************/

/* ***************************************************************************/
class WithAlertTests {

    companion object {
        @JvmStatic
        val pageWithAlerts = WithAlertTests::class.java
            .getResource("/test-pages/page-with-alerts.html")
            .toString()
    }

    @DataProvider(name = "JavaScript-enabled WebDriver factory", parallel = true)
    fun `Create a JavaScript-enabled WebDriver factory`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BROWSER_VERSION).apply { isJavascriptEnabled = true } })
    )

    @Test(description = "Validate context switching to and from an alert popup and accept it",
        dataProvider = "JavaScript-enabled WebDriver factory")
    fun validate_context_switching_to_and_from_an_alert_popup_and_accept_it(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which popup alerts
            to(pageWithAlerts)

            // And I'm in the context of the page
            assertEquals(`$`("h1", 0).text, "Page with Alerts")

            // When I click in the button which makes the alert appear
            `$`("#alert", 0).click()

            // And I change the driver's context to the alert
            withAlert {
                // Then I should be able to get the alert's text
                assertEquals(text, "Balin is awesome!")

                // And I accept the alert
                accept()
            }

            // Then I should return into the context of the page at the end of the `withAlert` method
            assertEquals(`$`("h1", 0).text, "Page with Alerts")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Validate context switching to and from an alert popup and auto-dismiss it`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which popup alerts
            to(pageWithAlerts)

            // And I'm in the context of the page
            assertEquals(`$`("h1", 0).text, "Page with Alerts")

            // When I click in the button which makes the alert appear
            `$`("#alert", 0).click()

            // And I change the driver's context to the alert
            withAlert {
                // Then I should be able to get the alert's text
                assertEquals(text, "Balin is awesome!")
            }

            // Then the alert should has been dismissed
            assertNull(alertIsPresent().apply(driver))
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Validate context switching to and from an confirm popup and accept it`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which popup alerts
            to(pageWithAlerts)

            // And I'm in the context of the page
            assertEquals(`$`("h1", 0).text, "Page with Alerts")

            // When I click in the button which makes the alert appear
            `$`("#confirm", 0).click()

            // And I change the driver's context to the alert
            withAlert {
                // Then I should be able to get the alert's text
                assertEquals(text, "Do you really think so?")

                // And I accept the confirm popup
                accept()
            }

            // Then I should return into the context of the page at the end of the `withAlert` method
            assertEquals(`$`("#feedback", 0).text, "true")
        }
    }

    @Test(dataProvider = "JavaScript-enabled WebDriver factory")
    fun `Validate context switching to and from an prompt popup and accept it`(driverFactory: () -> WebDriver) {
        Browser.drive(driverFactory) {
            // Given I navigate to the page under test, which popup alerts
            to(pageWithAlerts)

            // And I'm in the context of the page
            assertEquals(`$`("h1", 0).text, "Page with Alerts")

            // When I click in the button which makes the alert appear
            `$`("#prompt", 0).click()

            // And I change the driver's context to the alert
            withAlert {
                // Then I should be able to get the alert's text
                assertEquals(text, "How awesome is Balin for you?")

                // And I sent some text to the alert
                sendKeys("A lot!")

                // And I accept the prompt popup
                accept()
            }

            // Then I should return into the context of the page at the end of the `withAlert` method
            assertEquals(`$`("#feedback", 0).text, "A lot!")
        }
    }
}
/* ***************************************************************************/
