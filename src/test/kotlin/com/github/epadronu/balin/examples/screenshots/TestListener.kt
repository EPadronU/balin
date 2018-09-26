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
package com.github.epadronu.balin.examples.screenshots
/* ***************************************************************************/

/* ***************************************************************************/
import io.qameta.allure.Allure
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.testng.ITestListener
import org.testng.ITestResult
import org.testng.TestListenerAdapter
/* ***************************************************************************/

/* ***************************************************************************/
class TestListener(listener: ITestListener = TestListenerAdapter()) : BaseTest(), ITestListener by listener {

    override fun onTestSuccess(result: ITestResult) {
        takeScreenshots(result)
    }

    override fun onTestFailure(result: ITestResult) {
        takeScreenshots(result)
    }

    private fun takeScreenshots(result: ITestResult) {
        val takesScreenshot = (result.instance as? BaseTest)?.webDriver as? TakesScreenshot

        takesScreenshot?.run {
            Allure.addByteAttachmentAsync("Screenshots", "image/png") {
                getScreenshotAs(OutputType.BYTES)
            }
        }
    }
}
/* ***************************************************************************/
