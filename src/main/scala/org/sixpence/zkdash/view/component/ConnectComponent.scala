package org.sixpence.zkdash.view.component

import java.awt.event._
import java.awt._

import javax.swing.text.{AttributeSet, PlainDocument}
import javax.swing._
import org.sixpence.zkdash.view.component.builder.GridBagConstraintsBuilder
import org.sixpence.zkdash.wrapper.ZkClientWrapper
import reactor.core.publisher.Mono

/**
  * @author geksong
  * Created by geksong on 2019/2/26.
  */

class ConnectComponent(cf: ZkClientWrapper => Unit) extends JPanel(new GridBagLayout) {

  private val conLabelM = Mono.create[GridComponentContainer[JLabel]](sink => sink.success(new GridComponentContainer[JLabel](new JLabel("Connect to "), GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(1).weighty(0).gridx(1).gridy(1).build())))
  val ipText = new GridComponentContainer[JTextField](new JTextField("localhost"), GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(1).weighty(0).gridx(2).gridy(1).build())

  val portText = new GridComponentContainer[JTextField](new JTextField(new NumberDocument, "2181", 0), GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(1).weighty(0).gridx(3).gridy(1).build())


  val but = new JButton("Connect")
  but.setMnemonic(KeyEvent.VK_C)
  but.addActionListener(ae => {
    Option(ipText.component.getText).zip(Option(portText.component.getText)).map(x => s"${x._1}:${x._2}").foreach(zkServer => {
      val zkClient = new ZkClientWrapper(zkServer)
      sys.runtime.addShutdownHook(new Thread(() => zkClient.close()))
      cf(zkClient)
    })
  })
  private val connectButton = Mono.just(new GridComponentContainer[JButton](but, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridwidth(1).gridheight(1)
    .weightx(0).weighty(0).gridx(3).gridy(2).build()))


  conLabelM.asInstanceOf[Mono[GridComponentContainer[Component]]]
    .concatWith(Mono.just(ipText.asInstanceOf[GridComponentContainer[Component]]))
    .concatWith(Mono.just(portText.asInstanceOf[GridComponentContainer[Component]]))
    .concatWith(connectButton.asInstanceOf[Mono[GridComponentContainer[Component]]]).subscribe(a => {
    this.add(a.component, a.position)
  })

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
