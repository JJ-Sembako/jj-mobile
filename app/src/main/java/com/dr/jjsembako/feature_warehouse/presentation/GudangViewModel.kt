package com.dr.jjsembako.feature_warehouse.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dr.jjsembako.core.data.remote.response.product.DataProduct
import com.dr.jjsembako.feature_warehouse.data.WebSocketClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GudangViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient
) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    private val _dataProducts = MutableLiveData<List<DataProduct?>>()
    val dataProducts: LiveData<List<DataProduct?>> get() = _dataProducts

    init {
        initWebSocket()
    }

    private fun initWebSocket() {
        // Connect to WebSocket
        webSocketClient.connect()

        // Set up callbacks for WebSocket events
        webSocketClient.onProductsReceived = { products ->
            _dataProducts.value = products
            _loadingState.value = false
        }

        webSocketClient.onNewProductReceived = { newProduct ->
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            currentList.add(0, newProduct)
            _dataProducts.value = currentList
        }

        webSocketClient.onUpdateProductReceived = { updatedProducts ->
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            for (updatedProduct in updatedProducts) {
                val index = currentList.indexOfFirst { it?.id == updatedProduct.id }
                if (index != -1) {
                    currentList[index] = updatedProduct
                }
            }
            _dataProducts.value = currentList
        }

        webSocketClient.onDeleteProductReceived = { deletedProductId ->
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            currentList.removeAll { it?.id == deletedProductId }
            _dataProducts.value = currentList
        }

        webSocketClient.onErrorReceived = { error ->
            _loadingState.value = false
            _errorState.value = error
        }

        webSocketClient.onLoadingState= { it ->
            _loadingState.value = it
        }
    }

    override fun onCleared() {
        // Disconnect WebSocket when ViewModel is cleared
        webSocketClient.close()
        super.onCleared()
    }
}
