package com.dr.jjsembako.inventaris.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.inventaris.domain.model.Product
import com.dr.jjsembako.core.utils.DataMapper.mapListDataCategoryToListFilterOption
import com.dr.jjsembako.inventaris.data.SocketWarehouseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GudangViewModel @Inject constructor(
    private val socketWarehouseHandler: SocketWarehouseHandler
) : ViewModel() {

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData(false)
    val errorState: LiveData<Boolean> get() = _errorState

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    private val _dataProducts = MutableLiveData<List<Product?>>()
    val dataProducts: LiveData<List<Product?>> get() = _dataProducts

    private val _dataRawCategories = MutableLiveData<List<String?>>()
    private val dataRawCategories: LiveData<List<String?>> get() = _dataRawCategories

    private val _dataCategories = MutableLiveData<List<FilterOption?>>()
    val dataCategories: LiveData<List<FilterOption?>> get() = _dataCategories

    init {
        initSocket()
    }

    private fun updateCategories(newProducts: List<Product?>) {
        viewModelScope.launch {
            val newCategories = newProducts.mapNotNull { it?.category }.distinct()
            val currentCategories = _dataRawCategories.value.orEmpty().toMutableSet()

            newCategories.forEach {
                if (!currentCategories.contains(it)) {
                    currentCategories.add(it)
                }
            }

            _dataRawCategories.value = currentCategories.toList()
            _dataCategories.value = mapListDataCategoryToListFilterOption(dataRawCategories.value)
        }
    }

    private fun initSocket() {
        // Connect to Socket
        socketWarehouseHandler.connect()

        // Set up callbacks for Socket events
        socketWarehouseHandler.onProductsReceived = { products ->
            viewModelScope.launch {
                _dataProducts.value = products
                _loadingState.value = false
                updateCategories(products)
            }
        }

        socketWarehouseHandler.onNewProductReceived = { newProduct ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.add(0, newProduct)
                _dataProducts.value = currentList
                updateCategories(listOf(newProduct))
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
                updateCategories(updatedProducts)
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
                _errorMsg.value = error
            }
        }

        socketWarehouseHandler.onLoadingState = { it ->
            viewModelScope.launch {
                _loadingState.value = it
            }
        }

        socketWarehouseHandler.onErrorState = { it ->
            viewModelScope.launch {
                _errorState.value = it
            }
        }
    }

    override fun onCleared() {
        // Disconnect Socket when ViewModel is cleared
        socketWarehouseHandler.disconnect()
        super.onCleared()
    }
}
