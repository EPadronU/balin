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
import com.github.epadronu.balin.exceptions.MissingPageUrlException
import com.github.epadronu.balin.exceptions.PageImplicitAtVerificationException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
/* ***************************************************************************/

/* ***************************************************************************/
interface Browser : JavaScriptSupport, WaitingSupport, WebDriver {

    companion object {
        fun drive(driver: WebDriver = FirefoxDriver(), autoQuit: Boolean = true, block: Browser.() -> Unit) {
            BrowserImpl(driver).apply {
                block()

                if (autoQuit) {
                    quit()
                }
            }
        }
    }

    fun <T : Page> at(factory: (Browser) -> T): T = factory(this).apply {
        if (!verifyAt()) {
            throw PageImplicitAtVerificationException()
        }
    }

    fun <T : Page> to(factory: (Browser) -> T): T = factory(this).apply {
        get(url ?: throw MissingPageUrlException())

        if (!verifyAt()) {
            throw PageImplicitAtVerificationException()
        }
    }

    fun to(url: String): String {
        get(url)

        return currentUrl
    }
}
/* ***************************************************************************/
