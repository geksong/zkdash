package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient
import reactor.core.publisher.Mono

import scala.collection.JavaConverters._

/**
  * @author geksong
  * Created by geksong on 2019/2/22.
  */
class LsCommand(zkCli: ZkClient) {
  def execute(path: String): Mono[List[String]] = {
    Mono.create(sink => sink.success(zkCli.getChildren(path).asScala toList))
  }
}

object LsCommand {

  def apply(zkCli: ZkClient): LsCommand = new LsCommand(zkCli)
}
