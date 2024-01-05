package com.dr.jjsembako.feature_warehouse.data

import android.content.SharedPreferences
import android.util.Log
import com.dr.jjsembako.BuildConfig
import com.dr.jjsembako.core.data.remote.response.product.DataProduct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import io.socket.engineio.client.transports.WebSocket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketWarehouseHandler @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val TAG = "Socket-Product"
    }

    private lateinit var socket: Socket

    // Callbacks for events
    var onProductsReceived: ((List<DataProduct>) -> Unit)? = null
    var onNewProductReceived: ((DataProduct) -> Unit)? = null
    var onUpdateProductReceived: ((List<DataProduct>) -> Unit)? = null
    var onDeleteProductReceived: ((String) -> Unit)? = null
    var onErrorReceived: ((String) -> Unit)? = null
    var onLoadingState: ((Boolean) -> Unit)? = null

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        .readTimeout(5000, TimeUnit.SECONDS)
        .writeTimeout(5000, TimeUnit.SECONDS)
        .build()

    fun connect() {
        val token = sharedPreferences.getString("token", "")
        val options = IO.Options().apply {
            extraHeaders = mapOf("Authorization" to listOf("Bearer $token"))
//            path = "/ws/product"
            transports = arrayOf(WebSocket.NAME)
            reconnection = true
            reconnectionDelay = 1000
            reconnectionDelayMax = Integer.MAX_VALUE.toLong()
            reconnectionAttempts = 99999
        }
        try {
            IO.setDefaultOkHttpWebSocketFactory(client)
            IO.setDefaultOkHttpCallFactory(client)

            val socket = IO.socket("http://54.251.20.182:3000/ws/product", options)
            this.socket = socket
            socket.connect()

        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }

        socket.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "Socket connected")
            onLoadingState?.invoke(true)
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e(TAG, "Socket connection error")
            Log.e(TAG, "Cause error: ${it[0]}")
            Log.e(TAG, "Detail error: ${it.contentToString()}")
            onErrorReceived?.invoke("Gagal terhubung ke server!")
            onLoadingState?.invoke(false)
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d(TAG, "Socket disconnected")
            onErrorReceived?.invoke("Koneksi ke server terputus!")
            onLoadingState?.invoke(false)
        }

        socket.on("products") { args ->
            val products = gson.fromJson<List<DataProduct>>(
                args[0].toString(),
                object : TypeToken<List<DataProduct>>() {}.type
            )
            onProductsReceived?.invoke(products)
            onLoadingState?.invoke(false)
        }

        socket.on("new-product") { args ->
            val product = gson.fromJson<DataProduct>(args[0].toString(), DataProduct::class.java)
            onNewProductReceived?.invoke(product)
        }

        socket.on("update-product") { args ->
            val products = gson.fromJson<List<DataProduct>>(
                args[0].toString(),
                object : TypeToken<List<DataProduct>>() {}.type
            )
            onUpdateProductReceived?.invoke(products)
        }

        socket.on("delete-product") { args ->
            val productId = args[0].toString()
            onDeleteProductReceived?.invoke(productId)
        }
    }

    fun disconnect() {
        socket.disconnect()
    }

}