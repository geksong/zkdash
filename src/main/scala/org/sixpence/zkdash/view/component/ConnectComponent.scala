package org.sixpence.zkdash.view.component

import java.awt.event._
import java.awt.{GridBagConstraints, GridBagLayout, GridLayout, Toolkit}

import javax.swing.text.{AttributeSet, PlainDocument}
import javax.swing.{JButton, JLabel, JPanel, JTextField}
import org.sixpence.zkdash.view.component.builder.GridBagConstraintsBuilder
import org.sixpence.zkdash.wrapper.ZkClientWrapper
import reactor.core.publisher.Mono

/**
  * @author geksong
  * Created by geksong on 2019/2/26.
  */

class ConnectComponent(cf: ZkClientWrapper => Unit) extends JPanel(new GridBagLayout) {

  val zkIpLabel = new JLabel("Connect to ")
  this.add(zkIpLabel, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(1).weighty(0).gridx(1).gridy(1).build())


  val zkIpTextField = new JTextField("localhost")
  zkIpTextField.enableInputMethods(true)
  this.add(zkIpTextField, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(1).weighty(0).gridx(2).gridy(1).build())

  val zkPortTextField = new JTextField(new NumberDocument, "2181", 0)
  zkPortTextField.enableInputMethods(true)
  this.add(zkPortTextField, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(1).weighty(0).gridx(3).gridy(1).build())

  val conButton = new JButton("Connect")
  conButton.setMnemonic(KeyEvent.VK_C)
  conButton.addActionListener(ae => {
    Option(zkIpTextField.getText).zip(Option(zkPortTextField.getText)).map(t => s"${t._1}:${t._2}").foreach(zkServer => {
      val zkClient = new ZkClientWrapper(zkServer)
      sys.runtime.addShutdownHook(new Thread(() => zkClient.close()))
      cf(zkClient)
    })
  })
  this.add(conButton, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(0).weighty(0).gridx(3).gridy(2).build())

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
