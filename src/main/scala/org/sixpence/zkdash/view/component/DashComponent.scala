package org.sixpence.zkdash.view.component

import java.awt.{Dimension, GridBagConstraints, GridBagLayout}
import java.text.SimpleDateFormat

import javax.swing._
import org.sixpence.zkdash.command.{FetchDataCommand, LsCommand, ServerStateInfoCommand}
import org.sixpence.zkdash.wrapper.ZkClientWrapper

/**
  * @author geksong
  * Created by geksong on 2019/2/26.
  */

class DashComponent(zkClient: ZkClientWrapper) extends JPanel(new GridBagLayout) {
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
  gbConstraints.gridy = 4
  this.add(nodeScroll, gbConstraints)

  val pathInfoPanel = new JPanel()
  pathInfoPanel.setLayout(new BoxLayout(pathInfoPanel, BoxLayout.X_AXIS))
  val pathLabel = new JLabel("Path: ")

  val pathTextArea = new JTextArea("/", 2, 50)
  pathTextArea.setEditable(false)
  val pathTextAreaScroll = new JScrollPane(pathTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS)
  pathTextAreaScroll.setMinimumSize(new Dimension(100, 50))
  pathInfoPanel.add(pathLabel)
  pathInfoPanel.add(pathTextAreaScroll)
  gbConstraints.fill = GridBagConstraints.BOTH
  gbConstraints.gridwidth = 9
  gbConstraints.gridheight = 2
  gbConstraints.weightx = 1
  gbConstraints.weighty = 0
  gbConstraints.gridx = 3
  gbConstraints.gridy = 1
  this.add(pathInfoPanel, gbConstraints)

  val pathCreationPanel = new JPanel()
  val pathCreationLabel = new JLabel("CreateAt: ")
  pathCreationPanel.add(pathCreationLabel)
  gbConstraints.fill = GridBagConstraints.BOTH
  gbConstraints.gridwidth = 9
  gbConstraints.gridheight = 1
  gbConstraints.weightx = 1
  gbConstraints.weighty = 0
  gbConstraints.gridx = 3
  gbConstraints.gridy = 3
  this.add(pathCreationPanel, gbConstraints)

  gbConstraints.fill = GridBagConstraints.BOTH
  gbConstraints.gridwidth = 3
  gbConstraints.gridheight = 5
  gbConstraints.weightx = 0
  gbConstraints.weighty = 1
  gbConstraints.gridx = 0
  gbConstraints.gridy = 0
  this.add(new NodeComponent(new LsCommand(zkClient), new FetchDataCommand(zkClient), a => {
    nodeDataPane.setText(a.data.toString)
    pathTextArea.setText(s"${a.path}")
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
    this.add(a, gbConstraints)
  })

  def release(): Unit = {
    zkClient.close()
  }
}
