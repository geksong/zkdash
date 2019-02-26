package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import org.apache.zookeeper.CreateMode
import reactor.core.publisher.Mono

/**
  *
  * Created by bianshi on 2019/2/22.
  */
class CreatePersistCommand(zkCli: ZkClient) {
  def execute(path: String, data: Any): Mono[String] = {
    Mono.create(sink => sink.success(zkCli.create(path, data, CreateMode.PERSISTENT)))
  }
}

object CreatePersistCommand {
  def apply(zkCli: ZkClient): CreatePersistCommand = new CreatePersistCommand(zkCli)
}

case class Node(selfName: String, data: Any, children: Array[Node])
object Node {
  def apply(selfName: String, data: Any, children: Array[Node]): Node = new Node(selfName, data, children)

  def path2Node(path: List[String], data: Any): Node = {
    if(path.size == 1) {
      Node(path.head, data, null)
    }else {
      Node(path.head, null, Array(path2Node(path.tail, data)))
    }
  }
}
