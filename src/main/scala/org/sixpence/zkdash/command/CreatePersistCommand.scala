package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import org.apache.zookeeper.CreateMode
import reactor.core.publisher.Mono

/**
  *
  * @author geksong
  * Created by geksong on 2019/2/22.
  */
class CreatePersistCommand(zkCli: ZkClient) {
  def execute(path: String, data: Option[Any]): Mono[String] = {
    Mono.create(sink => sink.success(zkCli.create(path, data.orNull, CreateMode.PERSISTENT)))
  }
}

object CreatePersistCommand {
  def apply(zkCli: ZkClient): CreatePersistCommand = new CreatePersistCommand(zkCli)
}
