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
import org.openqa.selenium.By
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
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

    private val playMenu by lazy {
        waitFor { elementToBeClickable(By.xpath("//nav//li[*[text()='Play']]")) }
    }
    private val playgroundButton by lazy {
        waitFor { elementToBeClickable(By.xpath("//nav//li[*[text()='Playground']]")) }
    }

    fun goToTryItPage(): TryItPage {
        Actions(driver).moveToElement(playMenu).perform()
        return playgroundButton.click(::TryItPage)
    }
}

class TryItPage(browser: Browser) : Page(browser) {

    override val at = at {
        title.contains("Kotlin Playground")
    }

    private val consoleOutput by lazy {
        waitFor {
            ExpectedCondition { webDriver ->
                webDriver?.findElement(By.cssSelector(".code-output"))?.let { webElement ->
                    if (webElement.text.isEmpty()) null else webElement
                }
            }
        }
    }

    private val codeArea by lazy {
        By.cssSelector(".code-area")
    }

    private val runButton by lazy {
        waitFor { elementToBeClickable(By.cssSelector("button[data-test='run-button']")) }
    }

    fun runHelloWorld(): String {
        waitFor { visibilityOfElementLocated(codeArea) }
        runButton.click()

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
