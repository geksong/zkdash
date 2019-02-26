package org.sixpence.zkdash.view.component

import javax.swing.{JLabel, JPanel, SwingConstants}
import org.sixpence.zkdash.command.ServerStateInfoCommand
import reactor.core.publisher.Mono

/**
  *
  * Created by bianshi on 2019/2/25.
  */
class ServerStateInfoComponent(ssc: ServerStateInfoCommand) {
  def build(): Mono[JPanel] = {
    ssc.execute().map(a => {
      val infoPanel = new JPanel()
      val serversLabel = new JLabel(s"Servers: ${a.address}", SwingConstants.LEFT)
      //val listenersLabel = new JLabel(s"Listeners Count: ${a.numOfListeners}", SwingConstants.LEFT)
      infoPanel.add(serversLabel)
      //infoPanel.add(listenersLabel)
      infoPanel
    })
  }
}
