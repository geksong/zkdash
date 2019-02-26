package org.sixpence.zkdash.wrapper

import java.io._

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer

/**
  *
  * Created by bianshi on 2019/2/25.
  */
class ZkClientWrapper(zkServers: String, connectionTimeout: Int) extends ZkClient(zkServers, connectionTimeout){
  def this(zkServers: String) = {
    this(zkServers, 50000)
    this.setZkSerializer(new DefineSerializableSerializer)
  }
  def getServers(): String = {
    _connection.getServers
  }
}

class DefineSerializableSerializer extends ZkSerializer {
  override def serialize(data: Any): Array[Byte] = try {
    val byteArrayOS = new ByteArrayOutputStream
    val stream = new ObjectOutputStream(byteArrayOS)
    stream.writeObject(data)
    stream.close()
    byteArrayOS.toByteArray
  } catch {
    case e: IOException =>
      throw new ZkMarshallingError(e)
  }

  override def deserialize(bytes: Array[Byte]): AnyRef = try {
    if(null == bytes || bytes.length <= 0) return null
    val bais = new ByteArrayInputStream(bytes)
    try {
      val ois = new ObjectInputStream(bais)
      ois.readObject()
    }catch {
      case th: Throwable =>
        //th.printStackTrace()
        new String(bytes)
    }
  } catch {
    case e: ClassNotFoundException =>
      throw new ZkMarshallingError("Unable to find object class.", e)
    case e: IOException =>
      throw new ZkMarshallingError(e)
  }
}
