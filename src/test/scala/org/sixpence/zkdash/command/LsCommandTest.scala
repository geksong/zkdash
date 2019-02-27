package org.sixpence.zkdash.command

import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.I0Itec.zkclient.{ZkClient, ZkServer}
import org.apache.zookeeper.CreateMode
import org.scalatest.FlatSpec
import org.sixpence.zkdash.{BaseTest, TmpZkLocalServer}
import org.slf4j.LoggerFactory

import scala.util.Try

/**
  * @author geksong
  * Created by geksong on 2019/2/22.
  */
class LsCommandTest extends BaseTest{
  val log = LoggerFactory.getLogger(classOf[LsCommandTest])

  "LsCommand" should "list child paths when path exists" in {
    initServer(initCli => {
      Try{
        initCli.deleteRecursive("/d34")
        initCli.deleteRecursive("/url")
      }.recover({
        case e: ZkNoNodeException =>
        case _: Throwable =>
          shutdownServer()
          log.error("init zk server delete path node error")
          sys.exit(0)
      })

      initCli.create("/d34", None.orNull, CreateMode.PERSISTENT)
      initCli.create("/url", None.orNull, CreateMode.PERSISTENT)
      initCli.create("/d34/com.dis.soa.consumer.cart.center.api.IMaService", None.orNull, CreateMode.PERSISTENT)
    })

    val zkCli = new ZkClient(zkServer, connectionTimeout)
    val lsC = new LsCommand(zkCli)
    val parentPath = "/d34"
    val childs = lsC.execute(parentPath)
    childs.doFinally(_ => {
      zkCli.close()
      shutdownServer()
    }).subscribe(_.map(a => s"/${parentPath}/${a}").foreach(println(_)))
  }
}
