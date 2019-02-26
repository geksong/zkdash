package org.sixpence.zkdash.command

import java.io.{ByteArrayOutputStream, ObjectOutputStream}

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.sixpence.zkdash.BaseTest
import org.sixpence.zkdash.wrapper.DefineSerializableSerializer

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
  */
class WriteDataCommandTest extends BaseTest{
  "WriteDataCommand" should "success when write success" in {
    initServer(initCli => {
      try {
        initCli.deleteRecursive("/fdkkkd")
      }catch {
        case e: ZkNoNodeException =>
        case th: Throwable =>
          println("init server error. test will exit")
          th.printStackTrace()
          shutdownServer()
          sys.exit(0)
      }
      initCli.createPersistent("/fdkkkd")
    })

    val zkCli = new ZkClient(ZKSERVER, CONNECTION_TIMEOUT)
    zkCli.setZkSerializer( new DefineSerializableSerializer)

    new WriteDataCommand(zkCli).execute("/fdkkkd", "xyz")

    new FetchDataCommand(zkCli).execute("/fdkkkd").doFinally(signalType => {
      zkCli.close()
      shutdownServer()
    }).subscribe(a => a.data should be("xyz"))
  }
}
