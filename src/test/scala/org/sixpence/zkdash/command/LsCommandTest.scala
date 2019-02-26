package org.sixpence.zkdash.command

import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.I0Itec.zkclient.{ZkClient, ZkServer}
import org.apache.zookeeper.CreateMode
import org.scalatest.FlatSpec
import org.sixpence.zkdash.{BaseTest, TmpZkLocalServer}
import org.slf4j.LoggerFactory

/**
  *
  * Created by bianshi on 2019/2/22.
  */
class LsCommandTest extends BaseTest{
  val log = LoggerFactory.getLogger(classOf[LsCommandTest])

  "LsCommand" should "list child paths when path exists" in {
    initServer(initCli => {
      try {
        initCli.deleteRecursive("/dubbo")
        initCli.deleteRecursive("/url")
      }catch {
        case e: ZkNoNodeException =>
        case _: Throwable =>
          shutdownServer()
          log.error("init zk server delete path node error")
          sys.exit(0)
      }

      initCli.create("/dubbo", null, CreateMode.PERSISTENT)
      initCli.create("/url", null, CreateMode.PERSISTENT)
      initCli.create("/dubbo/com.dfire.soa.consumer.cart.center.api.ICartService", null, CreateMode.PERSISTENT)
    })

    val zkCli = new ZkClient(ZKSERVER, CONNECTION_TIMEOUT)
    val lsC = new LsCommand(zkCli)
    val parentPath = "/dubbo"
    val childs = lsC.execute(parentPath)
    childs.doFinally(_ => {
      zkCli.close()
      shutdownServer()
    }).subscribe(_.map(a => s"/${parentPath}/${a}").foreach(println(_)))
  }
}
