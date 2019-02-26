package org.sixpence.zkdash.command

import java.io.{ByteArrayOutputStream, ObjectOutputStream}

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.sixpence.zkdash.BaseTest
import org.sixpence.zkdash.wrapper.DefineSerializableSerializer

/**
  *
  * Created by bianshi on 2019/2/23.
  */
class WriteDataCommandTest extends BaseTest{
  "WriteDataCommand" should "success when write success" in {
    initServer(initCli => {
      try {
        initCli.deleteRecursive("/fuck")
      }catch {
        case e: ZkNoNodeException =>
        case th: Throwable =>
          println("init server error. test will exit")
          th.printStackTrace()
          shutdownServer()
          sys.exit(0)
      }
      initCli.createPersistent("/fuck")
    })

    val zkCli = new ZkClient(ZKSERVER, CONNECTION_TIMEOUT)
    zkCli.setZkSerializer( new DefineSerializableSerializer)

    new WriteDataCommand(zkCli).execute("/fuck", "xyz")

    new FetchDataCommand(zkCli).execute("/fuck").doFinally(signalType => {
      zkCli.close()
      shutdownServer()
    }).subscribe(a => a.data should be("xyz"))
  }

  /**
  "WriteData" should "success" in {
    val zkClient = new ZkClient("localhost:2181", 5000)
    zkClient.setZkSerializer(new DefineSerializableSerializer)
    val cpc = new CreatePersistCommand(zkClient)
    cpc.execute("/book", null).subscribe(_ => {
      cpc.execute("/book/斗破苍穹", "这是本很好的书，下载地址在这里").subscribe(a => println(a))
      cpc.execute("/book/鬼吹灯", "南派三叔的经典之作，下载地址在这里<a href=\"http://www.baidu.com\">here</a>").subscribe(a => println(a))
    })
    cpc.execute("/category", null).subscribe(_ => {
      cpc.execute("/category/社科", null).subscribe(_ => {
        cpc.execute("/category/社科/数学", "数学是人类进步的阶梯").subscribe(println(_))
      })
    })
  }*/
}
