package com.dr.jjsembako.feature_warehouse.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.data.remote.response.product.DataProduct
import com.dr.jjsembako.feature_warehouse.data.SocketWarehouseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GudangViewModel @Inject constructor(
    private val socketWarehouseHandler: SocketWarehouseHandler
) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    private val _dataProducts = MutableLiveData<List<DataProduct?>>()
    val dataProducts: LiveData<List<DataProduct?>> get() = _dataProducts

    init {
        initSocket()
    }

    private fun initSocket() {
        // Connect to Socket
        socketWarehouseHandler.connect()

        // Set up callbacks for Socket events
        socketWarehouseHandler.onProductsReceived = { products ->
            viewModelScope.launch {
                _dataProducts.value = products
                _loadingState.value = false
            }
        }

        socketWarehouseHandler.onNewProductReceived = { newProduct ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.add(0, newProduct)
                _dataProducts.value = currentList
            }
        }

        socketWarehouseHandler.onUpdateProductReceived = { updatedProducts ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                for (updatedProduct in updatedProducts) {
                    val index = currentList.indexOfFirst { it?.id == updatedProduct.id }
                    if (index != -1) {
                        currentList[index] = updatedProduct
                    }
                }
                _dataProducts.value = currentList
            }
        }

        socketWarehouseHandler.onDeleteProductReceived = { deletedProductId ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.removeAll { it?.id == deletedProductId }
                _dataProducts.value = currentList
            }
        }

        socketWarehouseHandler.onErrorReceived = { error ->
            viewModelScope.launch {
                _loadingState.value = false
                _errorState.value = error
            }
        }

        socketWarehouseHandler.onLoadingState = { it ->
            viewModelScope.launch {
                _loadingState.value = it
            }
        }
    }

    override fun onCleared() {
        // Disconnect Socket when ViewModel is cleared
        socketWarehouseHandler.disconnect()
        super.onCleared()
    }
}
