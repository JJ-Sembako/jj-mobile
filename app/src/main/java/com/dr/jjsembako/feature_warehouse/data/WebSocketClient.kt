package com.dr.jjsembako.feature_warehouse.data

import android.util.Log
import com.dr.jjsembako.core.data.remote.response.product.DataProduct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
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

    fun connect() {
        onLoadingState?.invoke(true)
        webSocket = client.newWebSocket(request, this)
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
        Log.d(TAG, "WebSocket closed")
        onLoadingState?.invoke(false)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "WebSocket failure", t)
        onErrorReceived?.invoke((t.message + "\n" + response?.message))
        onLoadingState?.invoke(false)
    }

}