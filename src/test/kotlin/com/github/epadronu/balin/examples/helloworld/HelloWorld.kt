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
package com.github.epadronu.balin.examples.helloworld
/* ***************************************************************************/

/* ***************************************************************************/
import com.github.epadronu.balin.core.Browser
import com.github.epadronu.balin.core.Page
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
import org.openqa.selenium.support.ui.ExpectedConditions.textMatches
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class IndexPage(browser: Browser) : Page(browser) {

    override val url = "http://kotlinlang.org/"

    override val at = at {
        title == "Kotlin Programming Language"
    }

    private val tryItButton by lazy {
        waitFor { elementToBeClickable(By.className("try-button")) }
    }

    fun goToTryItPage(): TryItPage = tryItButton.click(::TryItPage)
}

class TryItPage(browser: Browser) : Page(browser) {
    override val at = at {
        title == "Kotlin Playground"
    }

    private val consoleOutput by lazy {
        `$`(".code-output", 0)
    }

    private val runButton by lazy {
        waitFor { elementToBeClickable(By.className("wt-button_mode_primary")) }
    }

    fun runHelloWorld(): String {
        runButton.click()

        waitFor { textMatches(By.cssSelector(".code-output"), ".+".toPattern()) }

        return consoleOutput.text
    }
}
/* ***************************************************************************/

/* ***************************************************************************/
class HelloWorldTest {

    @BeforeClass
    fun `configure the driver`() {
        /* You may need to provide the path for Firefox as well as for the gecko-driver
         * is you wish to run the test
         */
    }

    @Test
    fun `Test the HelloWorld example`() {
        Browser.drive {
            val indexPage = to(::IndexPage)

            val tryItPage = indexPage.goToTryItPage()

            Assert.assertEquals(tryItPage.runHelloWorld(), "Hello, world!!!")
        }
    }
}
/* ***************************************************************************/
