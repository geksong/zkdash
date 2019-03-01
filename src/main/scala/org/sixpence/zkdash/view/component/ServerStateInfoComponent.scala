package org.sixpence.zkdash.view.component

import javax.swing.{JLabel, JPanel, SwingConstants}
import org.sixpence.zkdash.command.ServerStateInfoCommand
import reactor.core.publisher.Mono

/**
  * @author geksong
  * Created by geksong on 2019/2/25.
  */
class ServerStateInfoComponent(ssc: ServerStateInfoCommand) {
  def build(): Mono[JPanel] = {
    ssc.execute().map(a => {
      val infoPanel = new JPanel()
      val serversLabel = new JLabel(s"Servers: ${a.address}", SwingConstants.LEFT)
      infoPanel.add(serversLabel)
      infoPanel
    })
  }
}
