package org.sixpence.zkdash.command

import org.I0Itec.zkclient.ZkClient

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
  */
class WriteDataCommand(zkClient: ZkClient) {
  def execute(path: String, data: AnyRef): Unit = {
    zkClient.writeData(path, data)
  }
}

object WriteDataCommand {
  def apply(zkClient: ZkClient): WriteDataCommand = new WriteDataCommand(zkClient)
}

case class Xyz(name: String, age: Int) extends Serializable
