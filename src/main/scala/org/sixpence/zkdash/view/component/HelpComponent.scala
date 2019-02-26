package org.sixpence.zkdash.view.component

import java.awt.Dimension

import javax.swing._

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
  */
class HelpComponent{
  def build(): JScrollPane = {
    val ep = new JEditorPane("text/html", "hsfalsdjfj")
    ep.setEditable(true)
    val container = new JScrollPane(ep)
    container.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
    container.setPreferredSize(new Dimension(250,145))
    container.setMinimumSize(new Dimension(10, 10))
    container
  }
}
