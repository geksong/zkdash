package org.sixpence.zkdash.config

import java.util.Properties

/**
  *
  * Created by bianshi on 2019/2/28.
  */
object ConfigHolder {
  private[this] val properties = new Properties()
  properties.load(getClass.getClassLoader.getResourceAsStream("application.properties"))

  def getProperty(key: String, defaultValue: String): String = {
    properties.getProperty(key, defaultValue)
  }
}
