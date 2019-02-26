package org.sixpence.zkdash

import org.I0Itec.zkclient.{IDefaultNameSpace, ZkServer}

/**
  *
  * Created by bianshi on 2019/2/22.
  */
case class TmpZkLocalServer(port: Int, dataPath: String, logPath: String, defaultNameSpace: IDefaultNameSpace) {
  private[this] var zkServer: ZkServer = _

  def start = {
    zkServer = new ZkServer(dataPath, logPath, defaultNameSpace, port)
    zkServer.start()
  }

  def shutdown = {
    zkServer.shutdown()
  }
}

object TmpZkLocalServer {
  def apply(port: Int, dataPath: String, logPath: String, defaultNameSpace: IDefaultNameSpace): TmpZkLocalServer = new TmpZkLocalServer(port, dataPath, logPath, defaultNameSpace)
}
