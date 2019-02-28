package org.sixpence.zkdash.view

import javax.swing._
import org.sixpence.zkdash.view.component.DashFrame

/**
  *
  * @author geksong
  * Created by geksong on 2019/2/22.
  */
object MainApplication {
  def main(args: Array[String]): Unit = {

    System.setProperty("java.awt.im.style", "on-the-spot")
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

    SwingUtilities.invokeLater(() => {
      new DashFrame()
    })
  }
}
