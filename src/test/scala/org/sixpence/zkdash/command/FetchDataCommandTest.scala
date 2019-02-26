package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.sixpence.zkdash.BaseTest

/**
  *
  * Created by bianshi on 2019/2/23.
  */
class FetchDataCommandTest extends BaseTest{
  "FetchDataCommand" should "return data for path" in {
    initServer(initCli => {
      try {
        initCli.deleteRecursive("/root")
      }catch {
        case e: ZkNoNodeException =>
        case th: Throwable =>
          println(s"something wrong when init server. test will exit")
          th.printStackTrace()
          shutdownServer()
          sys.exit(0)
      }
      initCli.createPersistent("/root")
      initCli.createPersistent("/root/fuck", "fuck this h")
    })

    val zkCli = new ZkClient(ZKSERVER, CONNECTION_TIMEOUT)
    new FetchDataCommand(zkCli).execute("/root/fuck")
      .doFinally(_ => {
        zkCli.close()
        shutdownServer()
      })
      .doOnSuccessOrError((a, t) => {
        println(s"end with ${a} ${t}")
      })
      .subscribe(a => {
        println(a)
        a.data should equal("fuck this h")
      })
  }
}
