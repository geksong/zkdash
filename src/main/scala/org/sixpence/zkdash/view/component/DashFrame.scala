package org.sixpence.zkdash.view.component

import java.awt.event._
import java.awt._
import java.net.URI
import java.util.concurrent.atomic.AtomicInteger

import javax.swing._
import javax.swing.plaf.basic.BasicButtonUI
import org.sixpence.zkdash.config.ConfigHolder
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

    val tabPanel = new JTabbedPane()
    tabPanel.setPreferredSize(new Dimension(500, 400))

    val menuBar = new JMenuBar()
    val fileMenu = new JMenu("File")
    val fileMenuItem = new JMenuItem("Create new Session", KeyEvent.VK_T)
    val sessionCount = new AtomicInteger()
    fileMenuItem.addActionListener(ae => {
      val sessionPanel = new JPanel(new GridBagLayout)
      val bc = new GridBagConstraints()
      bc.fill = GridBagConstraints.BOTH
      bc.weightx = 1
      bc.weighty = 1
      sessionPanel.add(new ConnectComponent(zkCli => {
        sessionPanel.removeAll()
        sessionPanel.add(new DashComponent(zkCli), bc)
      }), bc)
      val index = sessionCount.getAndAdd(1)
      tabPanel.add(s"session#${index + 1}", sessionPanel)
      tabPanel.setTabComponentAt(tabPanel.indexOfComponent(sessionPanel), new ButtonTabPanel(tabPanel, {
        sessionPanel.getComponents.foreach({
            case d: DashComponent => d.release()
            case _ =>
          })
      }))
    })
    fileMenu.add(fileMenuItem)

    val helpMenu = new JMenu("Help")
    val helpIcon = new ImageIcon(this.getClass.getClassLoader.getResource("information_16x16.png").getFile)
    helpIcon.setImage(helpIcon.getImage.getScaledInstance(16, 16, Image.SCALE_DEFAULT))
    val helpMenuItem = new JMenuItem("Help", helpIcon)
    helpMenuItem.addActionListener(_ => {
      val dp = Desktop.getDesktop
      if(dp.isSupported(Desktop.Action.BROWSE)) {
        dp.browse(new URI(ConfigHolder.getProperty("menu.help-url", "https://github.com/geksong/zkdash/wiki")))
      }else {
        log.error("This system not support browser")
      }
    })
    helpMenu.add(helpMenuItem)
    menuBar.add(fileMenu)
    menuBar.add(helpMenu)

    this.setJMenuBar(menuBar)

    val tab1Panel = new JPanel(new GridBagLayout)
    val gbc = new GridBagConstraints()
    gbc.fill = GridBagConstraints.BOTH
    gbc.weightx = 1
    gbc.weighty = 1
    tab1Panel.add(new ConnectComponent(zkCli => {
      tab1Panel.removeAll()
      tab1Panel.add(new DashComponent(zkCli), gbc)
    }), gbc)
    val sessIdx = sessionCount.getAndAdd(1)
    tabPanel.add(s"session#${sessIdx + 1}", tab1Panel)
    tabPanel.setTabComponentAt(tabPanel.indexOfComponent(tab1Panel), new ButtonTabPanel(tabPanel, {
      tab1Panel.getComponents.foreach({
          case d: DashComponent => d.release()
          case _ =>
        })
    }))

    this.getContentPane add tabPanel

    this.pack()
    this.setVisible(true)
  }
}

/**
  * custome tab title view
  * @param tabPanel
  * @param layoutManager
  */
class ButtonTabPanel(tabPanel: JTabbedPane, closeCallback: => Unit) extends JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
  if(null == tabPanel) throw new NullPointerException("tabPanel can't be null")
  setOpaque(false)

  val label = new JLabel(){
    override def getText: String = {
      val i = tabPanel.indexOfTabComponent(ButtonTabPanel.this)
      if(i != -1) tabPanel.getTitleAt(i) else None.orNull
    }
  }

  add(label)
  label.setBorder(BorderFactory.createEmptyBorder(0,0,0,5))
  //add button
  val closeButton = new TabButton
  add(closeButton)
  setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0))

  class TabButton extends JButton with ActionListener {

    setPreferredSize(new Dimension(17, 17))
    setToolTipText("close this tab")
    setUI(new BasicButtonUI)
    setContentAreaFilled(false)
    setFocusable(false)
    setBorder(BorderFactory.createEtchedBorder())
    setBorderPainted(false)
    addMouseListener(new MouseAdapter {
      override def mouseEntered(e: MouseEvent): Unit = e.getComponent match {
        case b: AbstractButton => b.setBorderPainted(true)
      }

      override def mouseExited(e: MouseEvent): Unit = e.getComponent match {
        case b: AbstractButton => b.setBorderPainted(false)
      }
    })
    setRolloverEnabled(true)
    addActionListener(this)

    override def actionPerformed(e: ActionEvent): Unit = {
      val i = tabPanel.indexOfTabComponent(ButtonTabPanel.this)
      if (i != -1) {
        closeCallback
        tabPanel.remove(i)
      }
    }

    override def updateUI(): Unit = {}

    override def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      val gric = g.create().asInstanceOf[Graphics2D]
      if (getModel.isPressed) {
        gric.translate(1, 1)
      }
      gric.setStroke(new BasicStroke(2))
      gric.setColor(Color.BLACK)
      if (getModel.isRollover) {
        gric.setColor(Color.MAGENTA)
      }
      val delta = 6
      gric.drawLine(delta, delta, getWidth - delta - 1, getHeight - delta - 1)
      gric.drawLine(getWidth - delta - 1, delta, delta, getHeight - delta - 1)
      gric.dispose()
    }
  }
}
