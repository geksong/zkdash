package org.sixpence.zkdash.view

import java.awt.{Component, GridBagConstraints}

/**
  *
  * Created by bianshi on 2019/3/4.
  */
package object component {

  case class GridComponentContainer[T <: Component](component: T, position: GridBagConstraints)

}
