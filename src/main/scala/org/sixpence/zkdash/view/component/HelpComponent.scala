package org.sixpence.zkdash.view.component

import java.awt.{Component, Dimension}

import javax.swing._
import reactor.core.publisher.Mono

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
  */
class HelpComponent{
  def build(): Mono[JScrollPane] = {
    Mono.create[JEditorPane](sink => {
      val ep = new JEditorPane("text/html", "hsfalsdjfj")
      ep.setEditable(true)
      sink.success(ep)
    }).map(a => {
      val container = new JScrollPane(a)
      container.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
      container.setPreferredSize(new Dimension(250, 145))
      container.setMinimumSize(new Dimension(10, 10))
      container
    })
  }
}
