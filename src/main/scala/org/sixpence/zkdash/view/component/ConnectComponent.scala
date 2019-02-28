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

class ConnectComponent(cf: ZkClientWrapper => Unit) extends JPanel(new GridBagLayout) {
  private val gbc = new GridBagConstraints()
  gbc.fill = GridBagConstraints.BOTH
  gbc.gridwidth = 1
  gbc.gridheight = 1
  gbc.weightx = 1
  gbc.weighty = 0
  gbc.gridx = 1
  gbc.gridy = 1
  val zkIpLabel = new JLabel("Connect to ")
  this.add(zkIpLabel, gbc)

  gbc.fill = GridBagConstraints.BOTH
  gbc.gridwidth = 1
  gbc.gridheight = 1
  gbc.weightx = 1
  gbc.weighty = 0
  gbc.gridx = 2
  gbc.gridy = 1
  val zkIpTextField = new JTextField("localhost")
  zkIpTextField.enableInputMethods(true)
  this.add(zkIpTextField, gbc)

  gbc.fill = GridBagConstraints.BOTH
  gbc.gridwidth = 1
  gbc.gridheight = 1
  gbc.weightx = 1
  gbc.weighty = 0
  gbc.gridx = 3
  gbc.gridy = 1
  val zkPortTextField = new JTextField(new NumberDocument, "2181", 0)
  zkPortTextField.enableInputMethods(true)
  this.add(zkPortTextField, gbc)

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
    Option(zkIpTextField.getText).zip(Option(zkPortTextField.getText)).map(t => s"${t._1}:${t._2}").foreach(zkServer => {
      val zkClient = new ZkClientWrapper(zkServer)
      sys.runtime.addShutdownHook(new Thread(() => zkClient.close()))
      cf(zkClient)
    })
  })
  this.add(conButton, gbc)

}

class NumberDocument extends PlainDocument {
  override def insertString(offs: Int, str: String, a: AttributeSet): Unit = {
    if (!Character.isDigit(str.charAt(0))){}
    else {
      super.insertString(offs, str, a)
    }
  }
}
