package org.sixpence.zkdash.command

import org.sixpence.zkdash.wrapper.ZkClientWrapper
import reactor.core.publisher.Mono

/**
  * @author geksong
  * Created by geksong on 2019/2/25.
  */
class ServerStateInfoCommand(zkCli: ZkClientWrapper) {
  def execute(): Mono[ServerState] = {
    Mono.create(sink => sink.success(ServerState(
      zkCli.getServers(), //服务地址
      zkCli.numberOfListeners() //监听者数
    )))
  }
}

case class ServerState(address: String, numOfListeners: Int)
