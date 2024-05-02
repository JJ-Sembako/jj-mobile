package com.dr.jjsembako.feature_history.data

import android.content.SharedPreferences
import android.util.Log
import com.dr.jjsembako.BuildConfig
import com.dr.jjsembako.core.data.model.OrderableProduct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Named

class SocketModifyOrderHandler @Inject constructor(
    @Named("webSocket") private val client: OkHttpClient,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val TAG = "Socket-PNR"
    }

    private lateinit var socket: Socket

    // Callbacks for events
    var onProductsReceived: ((List<OrderableProduct>) -> Unit)? = null
    var onNewProductReceived: ((OrderableProduct) -> Unit)? = null
    var onUpdateProductReceived: ((List<OrderableProduct>) -> Unit)? = null
    var onDeleteProductReceived: ((String) -> Unit)? = null
    var onErrorReceived: ((String) -> Unit)? = null
    var onErrorState: ((Boolean) -> Unit)? = null
    var onLoadingState: ((Boolean) -> Unit)? = null

    fun connect() {
        val token = sharedPreferences.getString("token", "")
        val options = IO.Options().apply {
            extraHeaders = mapOf("Authorization" to listOf("Bearer $token"))
            transports = arrayOf(WebSocket.NAME)
            reconnection = true
            reconnectionDelay = 1000
            reconnectionDelayMax = Integer.MAX_VALUE.toLong()
            reconnectionAttempts = 99999
        }
        try {
            IO.setDefaultOkHttpWebSocketFactory(client)
            IO.setDefaultOkHttpCallFactory(client)

            val socket = IO.socket(BuildConfig.WS_URL + "product", options)
            this.socket = socket
            socket.connect()

        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }

        socket.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "Socket connected")
            onErrorState?.invoke(false)
            onLoadingState?.invoke(true)
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e(TAG, "Socket connection error")
            Log.e(TAG, "Cause error: ${it[0]}")
            onErrorReceived?.invoke("Terjadi masalah koneksi ke server!")
            onErrorState?.invoke(true)
            onLoadingState?.invoke(false)
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d(TAG, "Socket disconnected")
            onErrorState?.invoke(false)
            onLoadingState?.invoke(false)
        }

        socket.on("products") { args ->
            val products = gson.fromJson<List<OrderableProduct>>(
                args[0].toString(),
                object : TypeToken<List<OrderableProduct>>() {}.type
            )
            Log.d(TAG, "Get all data, total: ${products.size}")
            onProductsReceived?.invoke(products)
            onErrorState?.invoke(false)
            onLoadingState?.invoke(false)
        }

        socket.on("new-product") { args ->
            val product = gson.fromJson(args[0].toString(), OrderableProduct::class.java)
            Log.d(TAG, "Get new product with name: ${product.name}")
            onNewProductReceived?.invoke(product)
        }

        socket.on("update-product") { args ->
            val products = gson.fromJson<List<OrderableProduct>>(
                args[0].toString(),
                object : TypeToken<List<OrderableProduct>>() {}.type
            )
            Log.d(TAG, "Get updated product, total: ${products.size}")
            onUpdateProductReceived?.invoke(products)
        }

        socket.on("delete-product") { args ->
            val product = gson.fromJson(args[0].toString(), OrderableProduct::class.java)
            val productId = product.id
            Log.d(TAG, "Delete product ${product.name} with id: $productId")
            onDeleteProductReceived?.invoke(productId)
        }
    }

    fun disconnect() {
        socket.disconnect()
    }

}