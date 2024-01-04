package com.dr.jjsembako.feature_warehouse.data

import android.util.Log
import com.dr.jjsembako.core.data.remote.response.product.DataProduct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import okio.EOFException
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor(
    @Named("webSocket") private val client: OkHttpClient,
    @Named("wsProduct") private val request: Request,
    private val gson: Gson
) : WebSocketListener() {

    companion object {
        private const val TAG = "WS-Product"
        private const val CODE = 1000
    }

    private var webSocket: WebSocket? = null

    // Callbacks for events
    var onProductsReceived: ((List<DataProduct>) -> Unit)? = null
    var onNewProductReceived: ((DataProduct) -> Unit)? = null
    var onUpdateProductReceived: ((List<DataProduct>) -> Unit)? = null
    var onDeleteProductReceived: ((String) -> Unit)? = null
    var onErrorReceived: ((String) -> Unit)? = null
    var onLoadingState: ((Boolean) -> Unit)? = null

    val request2 = Request.Builder()
        .url("http://54.251.20.182:3000/ws/product")
        .addHeader("Origin", "http://54.251.20.182:3000")
        .addHeader("Connection","keep-alive")
        .addHeader("Accept", "application/json")
        .addHeader("Upgrade", "websocket")
        .addHeader("Connection", "Upgrade")
        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyNzk2YmE0OC1jNGQ2LTRjZDMtOTE4NS01ZWYzMDI1MzBjMDMiLCJyb2xlIjoiU0FMRVMiLCJpYXQiOjE3MDIxNzE2NDJ9.TRZdIe-zluqrlo-ZZLsoGLNRynFD2nZ9a8SxG1zwrhM")
        .build()

    val client2 = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        .retryOnConnectionFailure(true)
        .readTimeout(30000, TimeUnit.SECONDS)
        .writeTimeout(30000, TimeUnit.SECONDS)
        .build()

    fun connect() {
        onLoadingState?.invoke(true)
//        webSocket = client.newWebSocket(request, this)
        webSocket = client2.newWebSocket(request2, this)
    }

    fun close() {
        webSocket?.close(CODE, "Closed by client")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "WebSocket opened")
        onLoadingState?.invoke(false)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val dataProductType = object : TypeToken<DataProduct>() {}.type
        val dataProductListType = object : TypeToken<List<DataProduct>>() {}.type

        val jsonObject = JSONObject(text)
        val event = jsonObject.getString("event")
        val data = jsonObject.getString("data")

        when (event) {
            "products" -> {
                val products = gson.fromJson<List<DataProduct>>(data, dataProductListType)
                onProductsReceived?.invoke(products)
            }

            "new-product" -> {
                val product = gson.fromJson<DataProduct>(data, dataProductType)
                onNewProductReceived?.invoke(product)
            }

            "update-product" -> {
                val products = gson.fromJson<List<DataProduct>>(data, dataProductListType)
                onUpdateProductReceived?.invoke(products)
            }

            "delete-product" -> {
                val product = gson.fromJson<DataProduct>(data, dataProductType)
                val productId = product.id
                onDeleteProductReceived?.invoke(productId)
            }
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "WebSocket closing")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "WebSocket closed with code $code, reason: $reason")
        onLoadingState?.invoke(false)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e(TAG, "error with code ${response?.code} because ${response?.message}")
        Log.e(TAG, "WebSocket failure", t)

        if (t is EOFException) {
            // Handle unexpected end of stream error
            onErrorReceived?.invoke("Unexpected end of stream. The connection may have been closed.")
        } else {
            // Handle other errors
            val errorMessage = t.message ?: "Unknown error"
            val responseMessage = response?.message ?: "No response message"
            onErrorReceived?.invoke("$errorMessage\n$responseMessage")

        }

        onLoadingState?.invoke(false)
    }

}