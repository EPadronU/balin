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
package com.github.epadronu.balin.examples.screenshots
/* ***************************************************************************/

/* ***************************************************************************/
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
/* ***************************************************************************/

/* ***************************************************************************/
open class BaseTest {

    lateinit var webDriver: WebDriver

    @BeforeClass
    fun `configure the driver`() {
        /* You may need to provide the path for Firefox as well as for the gecko-driver
         * is you wish to run the test
         */
    }

    @BeforeMethod
    fun `create the driver`() {
        webDriver = FirefoxDriver()
    }

    @AfterMethod
    fun `quit the driver`() {
        webDriver.quit()
    }
}
/* ***************************************************************************/
