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
import com.github.epadronu.balin.config.Configuration
import com.github.epadronu.balin.config.ConfigurationSetup
import com.github.epadronu.balin.exceptions.PageAtValidationError
import org.openqa.selenium.WebDriver
/* ***************************************************************************/

/* ***************************************************************************/
interface Browser : JavaScriptSupport, WaitingSupport, WebDriver {
  companion object {
    private val configurationSetup: ConfigurationSetup

    init {
      @Suppress("UNCHECKED_CAST")
      val configurationClass = Class.forName("BalinConfiguration") as? Class<Configuration>

      configurationSetup = configurationClass?.newInstance()?.run {
        setups[System.getProperty("balin.setup.name") ?: "default"] ?: this
      } ?: ConfigurationSetup.DEFAULT
    }

    fun drive(driver: WebDriver = configurationSetup.driverFactory(), autoQuit: Boolean = configurationSetup.autoQuit, block: Browser.() -> Unit) {
      BrowserImpl(driver).apply {
        block()

        if (autoQuit) {
          quit()
        }
      }
    }
  }

  val driver: WebDriver

  fun <T : Page> at(factory: () -> T): T {
    return at(factory, false)
  }

  fun to(url: String): String {
    get(url)

    return currentUrl
  }

  fun <T : Page> to(factory: () -> T): T {
    return at(factory, true)
  }

  private fun <T : Page> at(factory: () -> T, shouldChangeUrl: Boolean): T {
    val page = factory()

    page.browser = this

    if (shouldChangeUrl) {
      page.url?.let { to(it) }
    }

    if (!page.verifyAt()) {
      throw PageAtValidationError()
    }

    return page
  }
}
/* ***************************************************************************/
