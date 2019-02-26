package org.sixpence.zkdash.view

import javax.swing._
import org.sixpence.zkdash.view.component.DashFrame

/**
  *
  * Created by bianshi on 2019/2/22.
  */
object MainApplication {
  def main(args: Array[String]): Unit = {


    SwingUtilities.invokeLater(() => {
      new DashFrame()
    })
  }
}
