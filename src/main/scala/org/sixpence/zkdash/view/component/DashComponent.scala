package org.sixpence.zkdash.view.component

import java.awt.{Dimension, GridBagConstraints, GridBagLayout}
import java.text.SimpleDateFormat

import javax.swing._
import org.sixpence.zkdash.command.{FetchDataCommand, LsCommand, ServerStateInfoCommand}
import org.sixpence.zkdash.wrapper.ZkClientWrapper

/**
  *
  * Created by bianshi on 2019/2/26.
  */
class DashComponent(zkClient: ZkClientWrapper) {
  def build(): JPanel = {
    val contentPanel = new JPanel(new GridBagLayout)
    val gbConstraints = new GridBagConstraints()

    val nodeDataPane = new JEditorPane("text/html", "")
    nodeDataPane.setEditable(false)
    val nodeScroll = new JScrollPane(nodeDataPane)
    nodeScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
    nodeScroll.setPreferredSize(new Dimension(250, 145))
    nodeScroll.setMinimumSize(new Dimension(10, 100))
    gbConstraints.fill = GridBagConstraints.BOTH
    gbConstraints.gridwidth = 9
    gbConstraints.gridheight = 1
    gbConstraints.weightx = 1
    gbConstraints.weighty = 1
    gbConstraints.gridx = 3
    gbConstraints.gridy = 2
    contentPanel.add(nodeScroll, gbConstraints)

    val pathInfoPanel = new JPanel()
    val pathLabel = new JLabel("Path: /")
    val pathCreationLabel = new JLabel("CreateAt: ")
    pathInfoPanel.add(pathLabel)
    pathInfoPanel.add(pathCreationLabel)
    gbConstraints.fill = GridBagConstraints.BOTH
    gbConstraints.gridwidth = 9
    gbConstraints.gridheight = 1
    gbConstraints.weightx = 1
    gbConstraints.weighty = 0
    gbConstraints.gridx = 3
    gbConstraints.gridy = 1
    contentPanel.add(pathInfoPanel, gbConstraints)

    gbConstraints.fill = GridBagConstraints.BOTH
    gbConstraints.gridwidth = 3
    gbConstraints.gridheight = 3
    gbConstraints.weightx = 1
    gbConstraints.weighty = 1
    gbConstraints.gridx = 0
    gbConstraints.gridy = 0
    contentPanel.add(new NodeComponent(new LsCommand(zkClient), new FetchDataCommand(zkClient), a => {
      nodeDataPane.setText(a.data.toString)
      pathLabel.setText(s"Path: ${a.path}")
      if (a.creationTime > 0) {
        val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        pathCreationLabel.setText(s"CreateAt: ${sdf.format(a.creationTime)}")
      } else {
        pathCreationLabel.setText("CreateAt: ")
      }
    }).build(), gbConstraints)

    new ServerStateInfoComponent(new ServerStateInfoCommand(zkClient)).build().subscribe(a => {
      gbConstraints.fill = GridBagConstraints.BOTH
      gbConstraints.gridwidth = 9
      gbConstraints.gridheight = 1
      gbConstraints.weightx = 1
      gbConstraints.weighty = 0
      gbConstraints.gridx = 3
      gbConstraints.gridy = 0
      contentPanel.add(a, gbConstraints)
    })

    contentPanel
  }
}
