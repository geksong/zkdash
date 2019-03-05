package org.sixpence.zkdash.view.component.builder

import java.awt.GridBagConstraints

/**
  *
  * Created by bianshi on 2019/3/4.
  */
class GridBagConstraintsBuilder {
  private[this] val gbConstraints: GridBagConstraints = new GridBagConstraints()

  def fill(fill: Int): GridBagConstraintsBuilder = {
    gbConstraints.fill = fill
    this
  }
  def gridwidth(gridwidth: Int): GridBagConstraintsBuilder = {
    gbConstraints.gridwidth = gridwidth
    this
  }
  def gridheight(gridheight: Int): GridBagConstraintsBuilder = {
    gbConstraints.gridheight = gridheight
    this
  }
  def weightx(weightx: Double): GridBagConstraintsBuilder = {
    gbConstraints.weightx = weightx
    this
  }
  def weighty(weighty: Double): GridBagConstraintsBuilder = {
    gbConstraints.weighty = weighty
    this
  }
  def gridx(gridx: Int): GridBagConstraintsBuilder = {
    gbConstraints.gridx = gridx
    this
  }
  def gridy(gridy: Int): GridBagConstraintsBuilder = {
    gbConstraints.gridy = gridy
    this
  }
  def build(): GridBagConstraints = gbConstraints
}

object GridBagConstraintsBuilder {
  def apply(): GridBagConstraintsBuilder = new GridBagConstraintsBuilder()
}
