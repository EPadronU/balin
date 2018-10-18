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
import org.openqa.selenium.support.ui.ExpectedCondition
/* ***************************************************************************/

/* ***************************************************************************/
/**
 * Describes the `waitFor` method support, which aims to ease the use of
 * [WebDriverWait][org.openqa.selenium.support.ui.WebDriverWait].
 *
 * @sample com.github.epadronu.balin.core.BrowserTests.wait_for_the_presence_of_an_element_that_should_be_there
 */
interface WaitingSupport {

    /**
     * Repeatedly applies the underground driver to the given function until
     * one of the following occurs:
     *
     * 1. the function returns neither null nor false
     * 2. the function throws an unignored exception
     * 3. the timeout expires
     * 4. the current thread is interrupted
     *
     * @param T the function's expected return type.
     * @param timeOutInSeconds the timeout in seconds when an expectation is called.
     * @param sleepInMillis the duration in milliseconds to sleep between polls.
     * @param isTrue the parameter to pass to the ExpectedCondition.
     * @return The function's return value if the function returned something different from null or false before the timeout expired.
     */
    fun <T> waitFor(timeOutInSeconds: Long, sleepInMillis: Long, isTrue: () -> ExpectedCondition<T>): T

    /**
     * Repeatedly applies the underground driver to the given function until
     * one of the following occurs:
     *
     * 1. the function returns neither null nor false
     * 2. the function throws an unignored exception
     * 3. the timeout expires
     * 4. the current thread is interrupted
     *
     * @param T the function's expected return type.
     * @param timeOutInSeconds the timeout in seconds when an expectation is called.
     * @param isTrue the parameter to pass to the ExpectedCondition.
     * @return The function's return value if the function returned something different from null or false before the timeout expired.
     */
    fun <T> waitFor(timeOutInSeconds: Long, isTrue: () -> ExpectedCondition<T>): T

    /**
     * Repeatedly applies the underground driver to the given function until
     * one of the following occurs:
     *
     * 1. the function returns neither null nor false
     * 2. the function throws an unignored exception
     * 3. the timeout expires
     * 4. the current thread is interrupted
     *
     * @param T the function's expected return type.
     * @param isTrue the parameter to pass to the ExpectedCondition.
     * @return The function's return value if the function returned something different from null or false before the timeout expired.
     */
    fun <T> waitFor(isTrue: () -> ExpectedCondition<T>): T
}
/* ***************************************************************************/
