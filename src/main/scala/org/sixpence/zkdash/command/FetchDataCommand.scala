package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import reactor.core.publisher.Mono

/**
  *
  * Created by bianshi on 2019/2/23.
  */
class FetchDataCommand(zkCli: ZkClient) {
  def execute(path: String): Mono[PathData] = {
    Mono.create(sink => {
      val creationTime = zkCli.getCreationTime(path)
      var data: Any = zkCli.readData(path)
      if(null == data) {
        data = "该节点无数据"
      }
      sink.success(PathData(path, creationTime, data))
    })
  }
}

object FetchDataCommand {
  def apply(zkCli: ZkClient): FetchDataCommand = new FetchDataCommand(zkCli)
}

case class PathData(path: String, creationTime: Long, data: Any)