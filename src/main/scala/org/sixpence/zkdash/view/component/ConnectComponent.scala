package org.sixpence.zkdash.view.component

import java.awt.event.{ActionEvent, ActionListener, KeyEvent}
import java.awt.{GridBagConstraints, GridBagLayout, GridLayout}

import javax.swing.text.{AttributeSet, PlainDocument}
import javax.swing.{JButton, JLabel, JPanel, JTextField}
import org.sixpence.zkdash.wrapper.ZkClientWrapper

/**
  * @author geksong
  * Created by geksong on 2019/2/26.
  */
class ConnectComponent(cf: ZkClientWrapper => Unit) {
  def build(): JPanel = {
    val contentPannel = new JPanel(new GridBagLayout)
    val gbc = new GridBagConstraints()
    gbc.fill = GridBagConstraints.BOTH
    gbc.gridwidth = 1
    gbc.gridheight = 1
    gbc.weightx = 1
    gbc.weighty = 0
    gbc.gridx = 1
    gbc.gridy = 1
    val zkIpLabel = new JLabel("Connect to ")
    contentPannel.add(zkIpLabel, gbc)

    gbc.fill = GridBagConstraints.BOTH
    gbc.gridwidth = 1
    gbc.gridheight = 1
    gbc.weightx = 1
    gbc.weighty = 0
    gbc.gridx = 2
    gbc.gridy = 1
    val zkIpTextField = new JTextField("localhost")
    contentPannel.add(zkIpTextField, gbc)

    gbc.fill = GridBagConstraints.BOTH
    gbc.gridwidth = 1
    gbc.gridheight = 1
    gbc.weightx = 1
    gbc.weighty = 0
    gbc.gridx = 3
    gbc.gridy = 1
    val zkPortTextField = new JTextField(new NumberDocument, "2181", 0)
    contentPannel.add(zkPortTextField, gbc)

    gbc.fill = GridBagConstraints.BOTH
    gbc.gridwidth = 1
    gbc.gridheight = 1
    gbc.weightx = 0
    gbc.weighty = 0
    gbc.gridx = 3
    gbc.gridy = 2
    val conButton = new JButton("Connect")
    conButton.setMnemonic(KeyEvent.VK_C)
    conButton.addActionListener(ae => {
      //connect to zk
      val ip = zkIpTextField.getText
      val port = zkPortTextField.getText
      if(null != ip && "" != ip && null != port && "" != port) {
        val zkServer = s"${ip}:${port}"
        val zkClient = new ZkClientWrapper(zkServer)
        sys.runtime.addShutdownHook(new Thread(() => zkClient.close()))
        cf(zkClient)
      }
    })
    contentPannel.add(conButton, gbc)

    contentPannel
  }
}

class NumberDocument extends PlainDocument {
  override def insertString(offs: Int, str: String, a: AttributeSet): Unit = {
    if (!Character.isDigit(str.charAt(0))) return
    else {
      super.insertString(offs, str, a)
      return
    }
  }
}
