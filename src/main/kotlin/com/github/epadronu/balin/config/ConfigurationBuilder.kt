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
 * Defines the builder used in the configuration DSL that can be interacted
 * with via the [com.github.epadronu.balin.core.Browser.configure] method.
 *
 * @see ConfigurationSetup
 * @sample com.github.epadronu.balin.config.ConfigurationTests.call_the_configure_method_and_make_changes
 *
 * @property autoQuit control whether the driver quits at the end of [com.github.epadronu.balin.core.Browser.drive].
 * @property driverFactory the factory that will create the driver to be used when invoking [com.github.epadronu.balin.core.Browser.drive].
 * @property waitForSleepTimeInMilliseconds control the amount of time between attempts when using [com.github.epadronu.balin.core.WaitingSupport.waitFor].
 * @property waitForTimeOutTimeInSeconds control the total amount of time to wait for a condition evaluated by [com.github.epadronu.balin.core.WaitingSupport.waitFor] to hold.
 * @property setups may contain configuration setups to be used according to the `balin.setup.name` system property.
 * @constructor Creates a new configuration builder.
 */
class ConfigurationBuilder {

    var autoQuit: Boolean = ConfigurationSetup.Default.autoQuit

    var driverFactory: () -> WebDriver = ConfigurationSetup.Default.driverFactory

    var waitForSleepTimeInMilliseconds: Long = ConfigurationSetup.Default.waitForSleepTimeInMilliseconds

    var waitForTimeOutTimeInSeconds: Long = ConfigurationSetup.Default.waitForTimeOutTimeInSeconds

    var setups: Map<String, ConfigurationSetup> = mapOf()

    /**
     * Creates a new configuration setup.
     *
     * @return a new configuration setup using the options provided to the builder.
     */
    internal fun build(): Configuration = Configuration(
        autoQuit, driverFactory, waitForSleepTimeInMilliseconds, waitForTimeOutTimeInSeconds, setups)
}
/* ***************************************************************************/
