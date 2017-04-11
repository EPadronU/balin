/******************************************************************************
 * Copyright 2017 Edinson E. Padrón Urdaneta
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

package com.github.epadronu.balin.core

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.github.epadronu.balin.extensions.`$`
import com.github.epadronu.balin.libs.delegatesTo
import org.jetbrains.spek.api.Spek
import org.openqa.selenium.WebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import kotlin.test.assertEquals

/**
 * @author Niels Falk
 */
class ModuleSpec : Spek({
    given("the Kotlin's website has shared navigation on multiple pages") {
        class Navigation(element: WebElement) : Module(element) {
            val links by lazy {
                `$`("a")
            }
        }

        class CommunityPage : Page() {
            override val url = "https://kotlinlang.org/community/"

            override val at = delegatesTo<Browser, Boolean> {
                title == "Kotlin Programming Language"
            }

            val navigation by lazy {
                `$`(".global-nav").module(::Navigation)
            }
        }

        class DocsPage : Page() {
            override val url = "http://kotlinlang.org/docs/reference/"

            override val at = delegatesTo<Browser, Boolean> {
                title == "Reference - Kotlin Programming Language"
            }

            val navigation by lazy {
                `$`(".global-nav").module(::Navigation)
            }
        }

        on("visiting such pages and getting the module") {
            var countNavigationLinksOnIndex: Int? = null
            var countNavigationLinksOnLear: Int? = null

            Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                to(::CommunityPage).apply {
                    countNavigationLinksOnIndex = navigation.links.size
                }
            }
            Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                to(::DocsPage).apply {
                    countNavigationLinksOnLear = navigation.links.size
                }
            }
            it("should count the same number of links") {
                assertEquals(countNavigationLinksOnIndex, countNavigationLinksOnLear)
            }
        }
    }

    given("the Kotlin's website navigation contains social icons") {
        class SocialLink(e: WebElement) : Module(e) {
            val icon by lazy {
                `$`("i").first().getAttribute("class").split(" ").find { it.startsWith("icon-") }
            }
        }

        class IndexPage : Page() {
            override val url = "http://kotlinlang.org/"

            override val at = delegatesTo<Browser, Boolean> {
                title == "Kotlin Programming Language"
            }

            val socialLinks by lazy {
                `$`(".social-links a").moduleList(::SocialLink)
            }
        }

        on("visiting such page and getting social-Links") {
            var icons: List<String?>? = null

            Browser.drive(driver = HtmlUnitDriver(BrowserVersion.FIREFOX_45)) {
                to(::IndexPage).apply {
                    icons = socialLinks.map { it.icon }
                }
            }

            it("should get icons") {
                assertEquals(listOf("icon-github", "icon-twitter", "icon-forum"), icons)
            }
        }
    }
})
