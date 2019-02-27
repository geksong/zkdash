package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.sixpence.zkdash.BaseTest
import org.sixpence.zkdash.wrapper.DefineSerializableSerializer

import scala.util.Try

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
  */
class WriteDataCommandTest extends BaseTest{
  "WriteDataCommand" should "success when write success" in {
    initServer(initCli => {
      Try{initCli.deleteRecursive("/fdkkkd")}.recover({
        case e: ZkNoNodeException =>
        case th: Throwable =>
          println("init server error. test will exit")
          th.printStackTrace()
          shutdownServer()
          sys.exit(0)
      })
      initCli.createPersistent("/fdkkkd")
    })

    val zkCli = new ZkClient(zkServer, connectionTimeout)
    zkCli.setZkSerializer( new DefineSerializableSerializer)

    new WriteDataCommand(zkCli).execute("/fdkkkd", Option("xyz"))

    new FetchDataCommand(zkCli).execute("/fdkkkd").doFinally(signalType => {
      zkCli.close()
      shutdownServer()
    }).subscribe(a => a.data should be("xyz"))
  }
}
