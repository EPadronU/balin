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
import com.github.epadronu.balin.core.LazyConditions
import com.github.epadronu.balin.core.LazyConditions.Companion.CLICKABLE
import com.github.epadronu.balin.core.LazyConditions.Companion.IS_PRESENT
import com.github.epadronu.balin.core.Page
import org.openqa.selenium.By
import org.openqa.selenium.NotFoundException
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
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

    val tryItButton by xpath("//a[@class='nav-item' and contains(text(),'Try Online')]")

    fun goToTryItPage() = tryItButton.click(::TryItPage)
}

class TryItPage(browser: Browser) : Page(browser) {

    override val at = at {
        title.contains("Kotlin Playground")
    }

    private val consoleOutput by cssSelector(".code-output", waitFor = IS_PRESENT,
            withElement ={ if (it.text.isEmpty()) throw NotFoundException("Test is still empty") else it } )

    private val runButton by className("wt-button_mode_primary")

    fun runHelloWorld(): String {
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
