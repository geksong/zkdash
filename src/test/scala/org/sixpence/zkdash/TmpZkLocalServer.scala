package org.sixpence.zkdash

import org.I0Itec.zkclient.{IDefaultNameSpace, ZkServer}

/**
  * @author geksong
  * Created by geksong on 2019/2/22.
  */
case class TmpZkLocalServer(port: Int, dataPath: String, logPath: String, defaultNameSpace: IDefaultNameSpace) {
  private[this] val zkServer: ZkServer = new ZkServer(dataPath, logPath, defaultNameSpace, port)

  def start = {
    zkServer.start()
  }

  def shutdown = {
    zkServer.shutdown()
  }
}

object TmpZkLocalServer {
  def apply(port: Int, dataPath: String, logPath: String, defaultNameSpace: IDefaultNameSpace): TmpZkLocalServer = new TmpZkLocalServer(port, dataPath, logPath, defaultNameSpace)
}
