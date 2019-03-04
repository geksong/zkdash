package org.sixpence.zkdash.view.component

import java.awt.{Dimension, GridBagConstraints, GridBagLayout}
import java.text.SimpleDateFormat

import javax.swing._
import org.sixpence.zkdash.command.{FetchDataCommand, LsCommand, ServerStateInfoCommand}
import org.sixpence.zkdash.view.component.builder.GridBagConstraintsBuilder
import org.sixpence.zkdash.wrapper.ZkClientWrapper

/**
  * @author geksong
  * Created by geksong on 2019/2/26.
  */

class DashComponent(zkClient: ZkClientWrapper) extends JPanel(new GridBagLayout) {

  val nodeDataPane = new JEditorPane("text/html", "")
  nodeDataPane.setEditable(false)
  val nodeScroll = new JScrollPane(nodeDataPane)
  nodeScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
  nodeScroll.setPreferredSize(new Dimension(250, 145))
  nodeScroll.setMinimumSize(new Dimension(10, 100))
  this.add(nodeScroll, GridBagConstraintsBuilder()
    .fill(GridBagConstraints.BOTH).gridwidth(9).gridheight(1).weightx(1)
    .weighty(1).gridx(3).gridy(4).build())

  val pathInfoPanel = new JPanel()
  pathInfoPanel.setLayout(new BoxLayout(pathInfoPanel, BoxLayout.X_AXIS))
  val pathLabel = new JLabel("Path: ")
  val pathTextArea = new JTextArea("/", 2, 50)
  pathTextArea.setEditable(false)
  val pathTextAreaScroll = new JScrollPane(pathTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS)
  pathTextAreaScroll.setMinimumSize(new Dimension(100, 50))
  pathInfoPanel.add(pathLabel)
  pathInfoPanel.add(pathTextAreaScroll)
  this.add(pathInfoPanel, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(9).gridheight(2)
    .weightx(1).weighty(0).gridx(3).gridy(1).build())

  val pathCreationPanel = new JPanel()
  val pathCreationLabel = new JLabel("CreateAt: ")
  pathCreationPanel.add(pathCreationLabel)
  this.add(pathCreationPanel, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(9).gridheight(1)
    .weightx(1).weighty(0).gridx(3).gridy(3).build())

  new NodeComponent(new LsCommand(zkClient), new FetchDataCommand(zkClient), a => {
    nodeDataPane.setText(a.data.toString)
    pathTextArea.setText(s"${a.path}")
    if (a.creationTime > 0) {
      val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
      pathCreationLabel.setText(s"CreateAt: ${sdf.format(a.creationTime)}")
    } else {
      pathCreationLabel.setText("CreateAt: ")
    }
  }).build().subscribe(a => {
    this.add(a, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(3).gridheight(5)
      .weightx(0).weighty(1).gridx(0).gridy(0).build())
  })

  new ServerStateInfoComponent(new ServerStateInfoCommand(zkClient)).build().subscribe(a => {
    this.add(a, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(9).gridheight(1)
      .weightx(1).weighty(0).gridx(3).gridy(0).build())
  })

  def release(): Unit = {
    zkClient.close()
  }
}
