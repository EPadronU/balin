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
import com.github.epadronu.balin.core.Browser
import com.github.epadronu.balin.core.Page
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions.*
/* ***************************************************************************/

/* ***************************************************************************/
class IndexPage(browser: Browser) : Page(browser) {
  override val url = "http://kotlinlang.org/"

  override val at = at {
    title == "Kotlin Programming Language"
  }

  private val tryItButton by lazy {
    `$`(".get-kotlin-button", 0)
  }

  fun goToTryItPage(): TryItPage {
    tryItButton.click()

    return browser.at(::TryItPage)
  }
}

class TryItPage(browser: Browser) : Page(browser) {
  override val at = at {
    title == "Simplest version | Try Kotlin"
  }

  private val consoleOutput by lazy {
    `$`(".standard-output", 0)
  }

  private val runButton by lazy {
    `$`("#runButton", 0)
  }

  fun runHelloWorld(): String {
    waitFor { elementToBeClickable(By.cssSelector("#runButton")) }

    runButton.click()

    waitFor { textToBe(By.cssSelector(".standard-output"), "Hello, world!") }

    return consoleOutput.text
  }
}
/* ***************************************************************************/

/* ***************************************************************************/
fun main(args: Array<String>) {
  Browser.drive {
    val indexPage = to(::IndexPage)

    val tryItPage = indexPage.goToTryItPage()

    assert("Hello, world!" == tryItPage.runHelloWorld())
  }
}
/* ***************************************************************************/
