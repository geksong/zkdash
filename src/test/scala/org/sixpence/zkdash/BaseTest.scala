package org.sixpence.zkdash

import org.I0Itec.zkclient.{IDefaultNameSpace, ZkServer}
import org.apache.zookeeper.CreateMode
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.LoggerFactory

/**
  *
  * Created by bianshi on 2019/2/22.
  */
class BaseTest extends FlatSpec with Matchers{
  val PORT = 2184
  val ZKSERVER = s"localhost:${PORT}"
  val CONNECTION_TIMEOUT = 50000
  private[this] var tmpServer: TmpZkLocalServer = _

  def initServer(defaultNameSpace: IDefaultNameSpace): Unit = {
    tmpServer = TmpZkLocalServer(PORT, "/tmp/zkdash/data", "/tmp/zkdash/log", defaultNameSpace)
    tmpServer.start
  }

  def shutdownServer(): Unit = {
    tmpServer.shutdown
  }
}
