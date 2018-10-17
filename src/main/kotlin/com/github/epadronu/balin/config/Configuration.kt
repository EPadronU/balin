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
import org.openqa.selenium.WebDriver
/* ***************************************************************************/

/* ***************************************************************************/
/**
 * This class defines a special kind of configuration setup that may contain
 * other configuration setups, to be used according to the `balin.setup.name`
 * system property.
 *
 * @see ConfigurationSetup
 * @sample com.github.epadronu.balin.config.ConfigurationTests.call_the_drive_method_with_a_development_setup_configuration_and_use_it
 *
 * @property autoQuit control whether the driver quits at the end of [com.github.epadronu.balin.core.Browser.drive].
 * @property driverFactory the factory that will create the driver to be used when invoking [com.github.epadronu.balin.core.Browser.drive].
 * @property waitForSleepTimeInMilliseconds control the amount of time between attempts when using [com.github.epadronu.balin.core.WaitingSupport.waitFor].
 * @property waitForTimeOutTimeInSeconds control the total amount of time to wait for a condition evaluated by [com.github.epadronu.balin.core.WaitingSupport.waitFor] to hold.
 * @property setups may contain configuration setups to be used according to the `balin.setup.name` system property.
 * @constructor Creates a new configuration setup
 */
data class Configuration(
    override val autoQuit: Boolean = ConfigurationSetup.Default.autoQuit,
    override val driverFactory: () -> WebDriver = ConfigurationSetup.Default.driverFactory,
    override val waitForSleepTimeInMilliseconds: Long = ConfigurationSetup.Default.waitForSleepTimeInMilliseconds,
    override val waitForTimeOutTimeInSeconds: Long = ConfigurationSetup.Default.waitForTimeOutTimeInSeconds,
    val setups: Map<String, ConfigurationSetup> = emptyMap()) : ConfigurationSetup
/* ***************************************************************************/
