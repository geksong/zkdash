package org.sixpence.zkdash.wrapper

import java.io._

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer

import scala.util.Try

/**
  * @author geksong
  * Created by geksong on 2019/2/25.
  */
class ZkClientWrapper(zkServers: String, connectionTimeout: Int) extends ZkClient(zkServers, connectionTimeout){
  def this(zkServers: String) = {
    this(zkServers, 5000)
    this.setZkSerializer(new DefineSerializableSerializer)
  }
  def getServers(): String = {
    _connection.getServers
  }
}

class DefineSerializableSerializer extends ZkSerializer {
  override def serialize(data: Any): Array[Byte] = {
    val t = Try{
      val byteArrayOS = new ByteArrayOutputStream
      val stream = new ObjectOutputStream(byteArrayOS)
      stream.writeObject(data)
      stream.close()
      byteArrayOS.toByteArray
    }
    if(t.isSuccess) t.get else throw new ZkMarshallingError(t.failed.get)
  }

  override def deserialize(bytes: Array[Byte]): AnyRef = {
    val t = Try{
      Option(bytes) match {
        case None => None.orNull
        case Some(v) if v.length <= 0 => None.orNull
        case Some(v) =>
          val t1 = Try{new ObjectInputStream(new ByteArrayInputStream(v)).readObject()}
          t1.getOrElse(new String(v))
      }
    }
    if(t.isSuccess) t.get else throw new ZkMarshallingError(t.failed.get)
  }
}
