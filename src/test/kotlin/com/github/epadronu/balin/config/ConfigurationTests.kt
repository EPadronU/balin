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
package com.github.epadronu.balin.config
/* ***************************************************************************/

/* ***************************************************************************/
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.github.epadronu.balin.core.Browser
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class ConfigurationTests {

    @DataProvider(name = "JavaScript-incapable WebDriver factory", parallel = true)
    fun `Create the no JavaScript-enabled WebDriver`() = arrayOf(
        arrayOf({ HtmlUnitDriver(BrowserVersion.FIREFOX_52) })
    )

    @AfterMethod
    fun cleanup() {
        Browser.configure {
            autoQuit = ConfigurationSetup.Default.autoQuit

            driverFactory = ConfigurationSetup.Default.driverFactory

            setups = mapOf()
        }

        System.clearProperty(Browser.BALIN_SETUP_NAME_PROPERTY)
    }

    @Test
    fun `Use the default configuration`() {
        Assert.assertEquals(Browser.desiredConfiguration, ConfigurationSetup.Default)
    }

    @Test
    fun `Call the configure method but don't modify a thing`() {
        Browser.configure { }

        Assert.assertEquals(Browser.desiredConfiguration, ConfigurationSetup.Default)
    }

    @Test(description = "Call the configure method and make changes",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun call_the_configure_method_and_make_changes(testFactory: () -> WebDriver) {
        val desiredConfigurationSetup = Configuration(false, testFactory)

        Browser.configure {
            autoQuit = desiredConfigurationSetup.autoQuit

            driverFactory = desiredConfigurationSetup.driverFactory
        }

        Assert.assertEquals(Browser.desiredConfiguration, desiredConfigurationSetup)
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the configure method with a default setup and use it implicitly`(testFactory: () -> WebDriver) {
        val defaultConfigurationSetup = Configuration(false, testFactory)

        Browser.configure {
            setups = mapOf(
                "default" to defaultConfigurationSetup
            )
        }

        Assert.assertEquals(Browser.desiredConfiguration, defaultConfigurationSetup)
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the configure method with a default setup and use it explicitly`(testFactory: () -> WebDriver) {
        val defaultConfigurationSetup = Configuration(false, testFactory)

        Browser.configure {
            setups = mapOf(
                "default" to defaultConfigurationSetup
            )
        }

        System.setProperty(Browser.BALIN_SETUP_NAME_PROPERTY, "default")

        Assert.assertEquals(Browser.desiredConfiguration, defaultConfigurationSetup)
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the configure method with a development setup and don't use it`(testFactory: () -> WebDriver) {
        val developmentConfigurationSetup = Configuration(false, testFactory)

        Browser.configure {
            setups = mapOf(
                "development" to developmentConfigurationSetup
            )
        }

        Assert.assertNotEquals(Browser.desiredConfiguration, developmentConfigurationSetup)
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the configure method with a development setup and use it`(testFactory: () -> WebDriver) {
        val developmentConfigurationSetup = Configuration(false, testFactory)

        Browser.configure {
            setups = mapOf(
                "development" to developmentConfigurationSetup
            )
        }

        System.setProperty(Browser.BALIN_SETUP_NAME_PROPERTY, "development")

        Assert.assertEquals(Browser.desiredConfiguration, developmentConfigurationSetup)
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the drive method with a desired configuration`(testFactory: () -> WebDriver) {
        val desiredConfigurationSetup = Configuration(false, testFactory)

        Browser.drive(desiredConfigurationSetup) {
            Assert.assertEquals(configurationSetup, desiredConfigurationSetup)
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the drive method with a default-setup configuration and use it implicitly`(testFactory: () -> WebDriver) {
        val defaultConfigurationSetup = Configuration(false, testFactory)

        val desiredConfigurationSetup = ConfigurationBuilder().apply {
            driverFactory = testFactory

            setups = mapOf(
                "default" to defaultConfigurationSetup
            )
        }.build()

        Browser.drive(desiredConfigurationSetup) {
            Assert.assertEquals(configurationSetup, defaultConfigurationSetup)
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the drive method with a default-setup configuration and use it explicitly`(testFactory: () -> WebDriver) {
        val defaultConfigurationSetup = Configuration(false, testFactory)

        val desiredConfigurationSetup = ConfigurationBuilder().apply {
            driverFactory = testFactory

            setups = mapOf(
                "default" to defaultConfigurationSetup
            )
        }.build()

        System.setProperty(Browser.BALIN_SETUP_NAME_PROPERTY, "default")

        Browser.drive(desiredConfigurationSetup) {
            Assert.assertEquals(configurationSetup, defaultConfigurationSetup)
        }
    }

    @Test(dataProvider = "JavaScript-incapable WebDriver factory")
    fun `Call the drive method with a development-setup configuration and don't use it`(testFactory: () -> WebDriver) {
        val developmentConfigurationSetup = Configuration(false, testFactory)

        val desiredConfigurationSetup = ConfigurationBuilder().apply {
            driverFactory = testFactory

            setups = mapOf(
                "development" to developmentConfigurationSetup
            )
        }.build()

        Browser.drive(desiredConfigurationSetup) {
            Assert.assertEquals(configurationSetup, desiredConfigurationSetup)
        }
    }

    @Test(description = "Call the drive method with a development-setup configuration and use it",
        dataProvider = "JavaScript-incapable WebDriver factory")
    fun call_the_drive_method_with_a_development_setup_configuration_and_use_it(testFactory: () -> WebDriver) {
        val developmentConfigurationSetup = Configuration(false, testFactory)

        val desiredConfigurationSetup = ConfigurationBuilder().apply {
            driverFactory = testFactory

            setups = mapOf(
                "development" to developmentConfigurationSetup
            )
        }.build()

        System.setProperty(Browser.BALIN_SETUP_NAME_PROPERTY, "development")

        Browser.drive(desiredConfigurationSetup) {
            Assert.assertEquals(configurationSetup, developmentConfigurationSetup)
        }
    }
}
/* ***************************************************************************/
