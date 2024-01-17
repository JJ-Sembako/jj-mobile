package com.dr.jjsembako.core.utils
//
//import androidx.datastore.core.CorruptionException
//import androidx.datastore.core.Serializer
//import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
//import com.dr.jjsembako.OrderProductProto
//import java.io.InputStream
//import java.io.OutputStream
//
//class OrderProductProtoSerializer : Serializer<OrderProductProto> {
//    override val defaultValue: OrderProductProto = OrderProductProto.getDefaultInstance()
//
//    override suspend fun readFrom(input: InputStream): OrderProductProto {
//        try {
//            return OrderProductProto.parseFrom(input)
//        } catch (exception: InvalidProtocolBufferException) {
//            throw CorruptionException("Cannot read proto.", exception)
//        }
//    }
//
//    override suspend fun writeTo(t: OrderProductProto, output: OutputStream) {
//        t.writeTo(output)
//    }
//}