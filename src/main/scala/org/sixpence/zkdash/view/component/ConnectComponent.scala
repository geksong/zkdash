package org.sixpence.zkdash.view.component

import java.awt.datatransfer.{DataFlavor, StringSelection, Transferable}
import java.awt.event._
import java.awt.{GridBagConstraints, GridBagLayout, GridLayout, Toolkit}

import javax.swing.text.{AttributeSet, PlainDocument}
import javax.swing.{JButton, JLabel, JPanel, JTextField}
import org.sixpence.zkdash.wrapper.ZkClientWrapper
import reactor.core.publisher.Mono

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
  zkIpTextField.addKeyListener(new KeyListener {
    override def keyTyped(e: KeyEvent): Unit = {}

    override def keyPressed(e: KeyEvent): Unit = {}

    override def keyReleased(ke: KeyEvent): Unit = {
      ke match {
        case e: KeyEvent if (e.getModifiersEx == InputEvent.META_DOWN_MASK && e.getKeyCode == KeyEvent.VK_V)
          || (e.getModifiersEx == InputEvent.CTRL_DOWN_MASK && e.getKeyCode == KeyEvent.VK_V) =>
          val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
          val trans: Transferable = clipboard.getContents(None.orNull)
          val setIpText: String => Unit = zkIpTextField.setText
          Option(trans).filter(a => a.isDataFlavorSupported(DataFlavor.stringFlavor))
            .map(a => a.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String])
            .foreach(setIpText)

        case e: KeyEvent if (e.getModifiersEx == InputEvent.META_DOWN_MASK && e.getKeyCode == KeyEvent.VK_C)
          || (e.getModifiersEx == InputEvent.CTRL_DOWN_MASK && e.getKeyCode == KeyEvent.VK_C) =>
          val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
          Option(zkIpTextField.getText).foreach(ipStr => clipboard.setContents(new StringSelection(ipStr), None.orNull))

        case _ =>
      }
    }
  })
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
  zkPortTextField.addKeyListener(new KeyListener {
    override def keyTyped(e: KeyEvent): Unit = {}

    override def keyPressed(e: KeyEvent): Unit = {}

    override def keyReleased(e: KeyEvent): Unit = {
      e match {
        case h: KeyEvent if (h.getModifiersEx == InputEvent.META_DOWN_MASK && h.getKeyCode == KeyEvent.VK_V)
          || (h.getModifiersEx == InputEvent.CTRL_DOWN_MASK && h.getKeyCode == KeyEvent.VK_V) =>
          val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
          val trans: Transferable = clipboard.getContents(None.orNull)
          val setIpText: String => Unit = zkPortTextField.setText
          Option(trans).filter(a => a.isDataFlavorSupported(DataFlavor.stringFlavor))
            .map(a => a.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String])
            .foreach(setIpText)

        case h: KeyEvent if (h.getModifiersEx == InputEvent.META_DOWN_MASK && h.getKeyCode == KeyEvent.VK_C)
          || (h.getModifiersEx == InputEvent.CTRL_DOWN_MASK && h.getKeyCode == KeyEvent.VK_C) =>
          val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
          Option(zkPortTextField.getText).foreach(ipStr => clipboard.setContents(new StringSelection(ipStr), None.orNull))

        case _ =>
      }
    }
  })
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

object ConnectComponent {
  def apply(cf: ZkClientWrapper => Unit): Mono[ConnectComponent] = Mono.create(sink => sink.success(new ConnectComponent(cf)))
}

class NumberDocument extends PlainDocument {
  override def insertString(offs: Int, str: String, a: AttributeSet): Unit = {
    if (!Character.isDigit(str.charAt(0))){}
    else {
      super.insertString(offs, str, a)
    }
  }
}
