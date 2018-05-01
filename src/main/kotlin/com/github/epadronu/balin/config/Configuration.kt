/* ***************************************************************************/
package com.github.epadronu.balin.config
/* ***************************************************************************/

/* ***************************************************************************/
open class Configuration : ConfigurationSetup by ConfigurationSetup.DEFAULT {
  open val setups: Map<String, ConfigurationSetup>
    get() = mapOf("default" to ConfigurationSetup.DEFAULT)
}
/* ***************************************************************************/
