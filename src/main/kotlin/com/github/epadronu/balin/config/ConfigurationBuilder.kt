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
/**
 * Defines the builder used in the configuration DSL that can be interacted
 * with via the [com.github.epadronu.balin.core.Browser.configure] method.
 *
 * @see ConfigurationSetup
 * @sample com.github.epadronu.balin.config.ConfigurationTests.call_the_configure_method_and_make_changes
 *
 * @property setups may contain configuration setups to be used according to the `balin.setup.name` system property.
 * @constructor Creates a new configuration builder.
 */
class ConfigurationBuilder : ConfigurationSetupBuilder() {

    var setups: Map<String, ConfigurationSetup> = mapOf()

    /**
     * Domain-Specific language that let's you create a configuration.
     *
     * @sample com.github.epadronu.balin.config.ConfigurationTests.call_the_drive_method_with_a_development_setup_configuration_and_use_it
     *
     * @param block here you can interact with the DSL.
     */
    fun setup(block: ConfigurationSetupBuilder.() -> Unit): ConfigurationSetup = ConfigurationSetupBuilder().apply {
        block()
    }.build()

    /**
     * Creates a new configuration.
     *
     * @return a new configuration setup using the options provided to the builder.
     */
    override fun build(): Configuration = Configuration(
        autoQuit, driverFactory, waitForSleepTimeInMilliseconds, waitForTimeOutTimeInSeconds, setups)
}
/* ***************************************************************************/
