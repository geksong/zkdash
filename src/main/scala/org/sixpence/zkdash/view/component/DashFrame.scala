package org.sixpence.zkdash.view.component

import java.awt.event.{ActionEvent, KeyEvent}
import java.awt.Toolkit

import javax.swing._
import org.slf4j.LoggerFactory

/**
  * @author geksong
  * Created by geksong on 2019/2/26.
  */
class DashFrame(title: String) extends JFrame(title) {
  private[this] val log = LoggerFactory.getLogger(classOf[DashFrame])
  def this() = {
    this("ZkDash")
    init()
  }
  def init() {
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    this.setLocation(screenSize.width / 3, screenSize.height / 4)
    val iconFile = this.getClass.getClassLoader.getResource("zkdashicon.png").getFile
    val imI = new ImageIcon(iconFile)
    this.setIconImage(imI.getImage)
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    val menuBar = new JMenuBar()
    val amenu = new JMenu("File")
    amenu.setMnemonic(KeyEvent.VK_X)
    amenu.getAccessibleContext.setAccessibleDescription("nothing todo")
    val amenuItem = new JMenuItem("Exit", KeyEvent.VK_T)
    amenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK))
    amenuItem.getAccessibleContext.setAccessibleDescription("this dosn't do nothing")
    amenu.add(amenuItem)
    menuBar.add(amenu)

    this.setJMenuBar(menuBar)

    val contentPanel = new ConnectComponent(zkCli => {
      this.getContentPane.removeAll()
      this.getContentPane.add(new DashComponent(zkCli).build())
      this.pack()
      this.setVisible(true)
    }).build()

    this.getContentPane add contentPanel

    this.pack()
    this.setVisible(true)
  }
}
