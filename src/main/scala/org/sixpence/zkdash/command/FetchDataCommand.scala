package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import reactor.core.publisher.Mono

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
  */
class FetchDataCommand(zkCli: ZkClient) {
  def execute(path: String): Mono[PathData] = {
    Mono.create(sink => {
      val creationTime = zkCli.getCreationTime(path)
      val rd: Any =  zkCli.readData(path)
      sink.success(PathData(path, creationTime, Option(rd).getOrElse("该节点无数据")))
    })
  }
}

object FetchDataCommand {
  def apply(zkCli: ZkClient): FetchDataCommand = new FetchDataCommand(zkCli)
}

case class PathData(path: String, creationTime: Long, data: Any)