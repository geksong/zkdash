package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.scalatest.FlatSpec
import org.sixpence.zkdash.BaseTest
import org.slf4j.LoggerFactory

/**
  *
  * Created by bianshi on 2019/2/22.
  */
class CreatePersistCommandTest extends BaseTest{
  val log = LoggerFactory.getLogger(classOf[CreatePersistCommandTest])

  "CreatePersistCommand" should "success when create an unexist node" in {
    val path = "/tests1/t1"
    val data = "hello"
    initServer(initCli => {
      try {
        initCli.deleteRecursive("/tests1")
      }catch {
        case e: ZkNoNodeException =>
        case _: Throwable =>
          shutdownServer()
          log.error("init zk server error")
          sys.exit(0)
      }
    })
    val zkClient = new ZkClient(ZKSERVER, CONNECTION_TIMEOUT)
    val command = CreatePersistCommand(zkClient)
    command.execute("/tests1", null).subscribe(_ => {
      command.execute(path, data).doFinally(_ => {
        zkClient.close()
        shutdownServer()
      }).subscribe(a => a should equal(path))
    })
  }
}

class NodeTest extends BaseTest {
  "Path2Node" should "success" in {
    val pa = List("dis", "h2", "m5", "ui")
    val nd = Node.path2Node(pa, "hello")
    println(nd)
  }
}
