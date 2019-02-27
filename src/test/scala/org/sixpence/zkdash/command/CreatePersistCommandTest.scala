package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.scalatest.FlatSpec
import org.sixpence.zkdash.BaseTest
import org.slf4j.LoggerFactory

import scala.util.Try

/**
  * @author geksong
  * Created by geksong on 2019/2/22.
  */
class CreatePersistCommandTest extends BaseTest{
  val log = LoggerFactory.getLogger(classOf[CreatePersistCommandTest])

  "CreatePersistCommand" should "success when create an unexist node" in {
    val path = "/tests1/t1"
    val data = "hello"
    initServer(initCli => {
      Try{
        initCli.deleteRecursive("/tests1")
      }.recover({
        case e: ZkNoNodeException =>
        case _: Throwable =>
          shutdownServer()
          log.error("init zk server error")
          sys.exit(0)
      })
    })
    val zkClient = new ZkClient(zkServer, connectionTimeout)
    val command = CreatePersistCommand(zkClient)
    command.execute("/tests1", None).subscribe(_ => {
      command.execute(path, Option(data)).doFinally(_ => {
        zkClient.close()
        shutdownServer()
      }).subscribe(a => a should equal(path))
    })
  }
}
