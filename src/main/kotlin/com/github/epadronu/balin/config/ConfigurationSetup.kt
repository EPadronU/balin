/* ***************************************************************************/
package com.github.epadronu.balin.config
/* ***************************************************************************/

/* ***************************************************************************/
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
/* ***************************************************************************/

/* ***************************************************************************/
interface ConfigurationSetup {
  val autoQuit: Boolean

  val driverFactory: () -> WebDriver

  companion object {
    val DEFAULT = object : ConfigurationSetup {
      override val autoQuit: Boolean
        get() = true

      override val driverFactory: () -> WebDriver
        get() = ::FirefoxDriver
    }
  }
}
/* ***************************************************************************/
