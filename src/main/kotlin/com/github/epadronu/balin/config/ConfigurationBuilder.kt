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
open class ConfigurationBuilder {

    open var autoQuit: Boolean = ConfigurationSetup.DEFAULT.autoQuit

    open var driverFactory: () -> WebDriver = ConfigurationSetup.DEFAULT.driverFactory

    open var setups: Map<String, ConfigurationSetup> = mapOf()

    fun build(): Configuration = object : Configuration() {
        override val autoQuit: Boolean = this@ConfigurationBuilder.autoQuit

        override val driverFactory: () -> WebDriver = this@ConfigurationBuilder.driverFactory

        override val setups: Map<String, ConfigurationSetup> = this@ConfigurationBuilder.setups
    }
}
/* ***************************************************************************/
