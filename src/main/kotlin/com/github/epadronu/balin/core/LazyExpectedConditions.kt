package com.github.epadronu.balin.core

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions

class LazyConditions {
    companion object {
        val CLICKABLE = ::isClickable

        private fun isClickable(by: By, page: Page, withElement: ((WebElement) -> WebElement)? = null) = lazy {
            if(withElement != null) {
                throw IllegalArgumentException("withElement block not supported when using LazyConditions.CLICKABLE")
            }
            page.waitFor { ExpectedConditions.elementToBeClickable(by) } }

        val IS_PRESENT = ::isPresent

        private fun isPresent(by: By, page: Page, withElement: ((WebElement) -> WebElement)? = null) = lazy {
            page.waitFor {
                ExpectedCondition { webDriver ->
                    val webElement = webDriver?.findElement(by)
                    if(withElement != null) webElement?.let(withElement) else webElement
                }
            }
        }
    }

}