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
    ssc.execute().flatMap(a => Mono.create[JLabel](sink => sink.success(new JLabel(s"Servers: ${a.address}", SwingConstants.LEFT))).map(b => {
        val infoPanel = new JPanel()
        infoPanel.add(b)
        infoPanel
    }))
  }
}
