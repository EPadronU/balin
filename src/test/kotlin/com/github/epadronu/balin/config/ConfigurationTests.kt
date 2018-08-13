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
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class ConfigurationTests {

    @AfterMethod
    fun cleanup() {
        Browser.configure {
            autoQuit = ConfigurationSetup.DEFAULT.autoQuit

            driverFactory = ConfigurationSetup.DEFAULT.driverFactory

            setups = mapOf()
        }

        System.clearProperty("balin.setup.name")
    }

    @Test
    fun `Use the default configuration`() {
        Assert.assertEquals(Browser.configurationSetup.autoQuit, ConfigurationSetup.DEFAULT.autoQuit)

        Assert.assertEquals(Browser.configurationSetup.driverFactory, ConfigurationSetup.DEFAULT.driverFactory)
    }

    @Test
    fun `Call the configure method but don't modify a thing`() {
        Browser.configure { }

        Assert.assertEquals(Browser.configurationSetup.autoQuit, ConfigurationSetup.DEFAULT.autoQuit)

        Assert.assertEquals(Browser.configurationSetup.driverFactory, ConfigurationSetup.DEFAULT.driverFactory)
    }

    @Test
    fun `Call the configure method and modify autoQuit and driverFactory`() {
        val noAutoQuit = false

        val htmlUnitDriverFactory = { HtmlUnitDriver(BrowserVersion.FIREFOX_52) }

        Browser.configure {
            autoQuit = noAutoQuit

            driverFactory = htmlUnitDriverFactory
        }

        Assert.assertNotEquals(Browser.configurationSetup.autoQuit, ConfigurationSetup.DEFAULT.autoQuit)

        Assert.assertNotEquals(Browser.configurationSetup.driverFactory, ConfigurationSetup.DEFAULT.driverFactory)

        Assert.assertEquals(Browser.configurationSetup.autoQuit, noAutoQuit)

        Assert.assertEquals(Browser.configurationSetup.driverFactory, htmlUnitDriverFactory)
    }

    @Test
    fun `Call the configure method with a development setup and don't use it`() {
        val developmentConfigurationSetup = object : ConfigurationSetup {
            override val autoQuit: Boolean = false

            override val driverFactory: () -> WebDriver = {
                HtmlUnitDriver(BrowserVersion.FIREFOX_52)
            }
        }

        Browser.configure {
            setups = mapOf(
                "development" to developmentConfigurationSetup
            )
        }

        Assert.assertEquals(Browser.configurationSetup.autoQuit, ConfigurationSetup.DEFAULT.autoQuit)

        Assert.assertEquals(Browser.configurationSetup.driverFactory, ConfigurationSetup.DEFAULT.driverFactory)

        Assert.assertNotEquals(Browser.configurationSetup.autoQuit, developmentConfigurationSetup.autoQuit)

        Assert.assertNotEquals(Browser.configurationSetup.driverFactory, developmentConfigurationSetup.driverFactory)
    }

    @Test
    fun `Call the configure method with a development setup and use it`() {
        val developmentConfigurationSetup = object : ConfigurationSetup {
            override val autoQuit: Boolean = false

            override val driverFactory: () -> WebDriver = {
                HtmlUnitDriver(BrowserVersion.FIREFOX_52)
            }
        }

        Browser.configure {
            setups = mapOf(
                "development" to developmentConfigurationSetup
            )
        }

        System.setProperty("balin.setup.name", "development")

        Assert.assertNotEquals(Browser.configurationSetup.autoQuit, ConfigurationSetup.DEFAULT.autoQuit)

        Assert.assertNotEquals(Browser.configurationSetup.driverFactory, ConfigurationSetup.DEFAULT.driverFactory)

        Assert.assertEquals(Browser.configurationSetup.autoQuit, developmentConfigurationSetup.autoQuit)

        Assert.assertEquals(Browser.configurationSetup.driverFactory, developmentConfigurationSetup.driverFactory)
    }

    @Test
    fun `Call the drive method with a configuration and modify autoQuit and driverFactory`() {
        val customConfiguration = object : Configuration() {

            override val autoQuit: Boolean = false

            override val driverFactory: () -> WebDriver = { HtmlUnitDriver(BrowserVersion.FIREFOX_52) }
        }

        Browser.drive(customConfiguration) { }

        Assert.assertNotEquals(Browser.configurationSetup.autoQuit, ConfigurationSetup.DEFAULT.autoQuit)

        Assert.assertNotEquals(Browser.configurationSetup.driverFactory, ConfigurationSetup.DEFAULT.driverFactory)

        Assert.assertEquals(Browser.configurationSetup.autoQuit, customConfiguration.autoQuit)

        Assert.assertEquals(Browser.configurationSetup.driverFactory, customConfiguration.driverFactory)
    }

    @Test
    fun `Call the drive method with a development-setup configuration and don't use it`() {
        val developmentConfigurationSetup = object : ConfigurationSetup {
            override val autoQuit: Boolean = false

            override val driverFactory: () -> WebDriver = {
                HtmlUnitDriver(BrowserVersion.FIREFOX_52)
            }
        }

        val customConfiguration = object : Configuration() {

            override val autoQuit: Boolean = false

            override val driverFactory: () -> WebDriver = { HtmlUnitDriver(BrowserVersion.FIREFOX_52) }

            override val setups: Map<String, ConfigurationSetup> = mapOf(
                "development" to developmentConfigurationSetup
            )
        }

        Browser.drive(customConfiguration) {}

        Assert.assertEquals(Browser.configurationSetup.autoQuit, customConfiguration.autoQuit)

        Assert.assertEquals(Browser.configurationSetup.driverFactory, customConfiguration.driverFactory)

        Assert.assertNotEquals(Browser.configurationSetup, developmentConfigurationSetup)
    }

    @Test
    fun `Call the drive method with a development-setup configuration and use it`() {
        val developmentConfigurationSetup = object : ConfigurationSetup {
            override val autoQuit: Boolean = false

            override val driverFactory: () -> WebDriver = {
                HtmlUnitDriver(BrowserVersion.FIREFOX_52)
            }
        }

        val customConfiguration = object : Configuration() {

            override val autoQuit: Boolean = false

            override val driverFactory: () -> WebDriver = { HtmlUnitDriver(BrowserVersion.FIREFOX_52) }

            override val setups: Map<String, ConfigurationSetup> = mapOf(
                "development" to developmentConfigurationSetup
            )
        }

        System.setProperty("balin.setup.name", "development")

        Browser.drive(customConfiguration) {}

        Assert.assertEquals(Browser.configurationSetup, developmentConfigurationSetup)
    }
}
/* ***************************************************************************/
