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
import com.github.epadronu.balin.core.SLEEP_TIME_IN_MILLISECONDS
import com.github.epadronu.balin.core.TIME_OUT_TIME_IN_SECONDS
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
/* ***************************************************************************/

/* ***************************************************************************/
/**
 * This interface describe the different configuration options that can be used
 * to customize Balin's behavior.
 */
interface ConfigurationSetup {

    /**
     * Control whether the driver quits at the end
     * of [com.github.epadronu.balin.core.Browser.drive].
     *
     *     autoQuit = false
     */
    val autoQuit: Boolean

    /**
     * The factory that will create the driver to be used when invoking
     * [com.github.epadronu.balin.core.Browser.drive].
     *
     *     driverFactory = ::FirefoxDriver
     */
    val driverFactory: () -> WebDriver

    /**
     * Control the amount of time between attempts when using
     * [com.github.epadronu.balin.core.WaitingSupport.waitFor].
     *
     *     waitForSleepTimeInMilliseconds = 1_000L // One second
     */
    val waitForSleepTimeInMilliseconds: Long

    /**
     * Control the total amount of time to wait for a condition evaluated by
     * [com.github.epadronu.balin.core.WaitingSupport.waitFor] to hold.
     *
     *     waitForTimeOutTimeInSecond = 10L // Ten seconds
     */
    val waitForTimeOutTimeInSeconds: Long

    /**
     * Contains the default configuration setup used by Balin.
     */
    companion object {

        /**
         * Define the default configuration setup used by Balin.
         */
        internal val Default = Configuration(
            true, ::FirefoxDriver, SLEEP_TIME_IN_MILLISECONDS, TIME_OUT_TIME_IN_SECONDS)
    }
}
/* ***************************************************************************/
