package com.dr.jjsembako.feature_order.presentation.select_product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.utils.DataMapper
import com.dr.jjsembako.feature_order.data.SocketOrderHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihBarangViewModel @Inject constructor(
    private val socketOrderHandler: SocketOrderHandler
) : ViewModel() {

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData(false)
    val errorState: LiveData<Boolean> get() = _errorState

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    private val _dataProducts = MutableLiveData<List<DataProductOrder?>>()
    val dataProducts: LiveData<List<DataProductOrder?>> get() = _dataProducts

    private val _dataRawCategories = MutableLiveData<List<String?>>()
    private val dataRawCategories: LiveData<List<String?>> get() = _dataRawCategories

    private val _dataCategories = MutableLiveData<List<FilterOption?>>()
    val dataCategories: LiveData<List<FilterOption?>> get() = _dataCategories

    init {
        initSocket()
    }

    private fun updateCategories(newProducts: List<DataProductOrder?>) {
        viewModelScope.launch {
            val newCategories = newProducts.mapNotNull { it?.category }.distinct()
            val currentCategories = _dataRawCategories.value.orEmpty().toMutableSet()

            newCategories.forEach {
                if (!currentCategories.contains(it)) {
                    currentCategories.add(it)
                }
            }

            _dataRawCategories.value = currentCategories.toList()
            _dataCategories.value =
                DataMapper.mapListDataCategoryToListFilterOption(dataRawCategories.value)
        }
    }

    private fun updateProductsWithCheck(updatedProducts: List<DataProductOrder>) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()

            for (updatedProduct in updatedProducts) {
                val index = currentList.indexOfFirst { it?.id == updatedProduct.id }

                if (index != -1) {
                    val existingProduct = currentList[index]!!

                    val updatedExistingProduct = existingProduct.copy(
                        name = updatedProduct.name,
                        image = updatedProduct.image,
                        category = updatedProduct.category,
                        unit = updatedProduct.unit,
                        standardPrice = updatedProduct.standardPrice,
                        amountPerUnit = updatedProduct.amountPerUnit,
                        stockInPcs = updatedProduct.stockInPcs,
                        stockInUnit = updatedProduct.stockInUnit,
                        stockInPcsRemaining = updatedProduct.stockInPcsRemaining
                    )

                    currentList[index] = updatedExistingProduct
                    currentList.remove(existingProduct)
                } else {
                    currentList.add(updatedProduct)
                }
            }

            _dataProducts.value = currentList
            updateCategories(updatedProducts)
        }
    }

    fun updateOrderTotalPrice(product: DataProductOrder, total: String) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (total.isNotEmpty()) {
                    val orderTotalPrice = total.toLong()
                    val updatedExistingProduct = existingProduct.copy(
                        orderTotalPrice = orderTotalPrice,
                        orderPrice = orderTotalPrice / existingProduct.orderQty
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                } else {
                    disableOrder(existingProduct)
                }

                _dataProducts.value = currentList
            }
        }
    }

    fun updateOrderPrice(product: DataProductOrder, price: String) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (price.isNotEmpty()) {
                    val orderPrice = price.toLong()
                    val updatedExistingProduct = existingProduct.copy(
                        orderPrice = orderPrice,
                        orderTotalPrice = existingProduct.orderQty * orderPrice
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                } else {
                    disableOrder(existingProduct)
                }
            }

            _dataProducts.value = currentList
        }
    }

    fun updateOrderQty(product: DataProductOrder, qty: String) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (qty.isNotEmpty()) {
                    val orderQty = qty.toInt()
                    val updatedExistingProduct = existingProduct.copy(
                        orderQty = orderQty,
                        orderTotalPrice = existingProduct.orderPrice * orderQty
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                } else {
                    disableOrder(existingProduct)
                }
            }

            _dataProducts.value = currentList
        }
    }

    fun minusOrderQty(product: DataProductOrder) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.orderQty > 0) {
                    val updatedExistingProduct = existingProduct.copy(
                        orderQty = existingProduct.orderQty - 1,
                        orderTotalPrice = existingProduct.orderPrice * (existingProduct.orderQty - 1)
                    )
                    if (updatedExistingProduct.orderQty == 0) disableOrder(existingProduct)
                    else {
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)
                    }
                }
            }

            _dataProducts.value = currentList
        }
    }

    fun plusOrderQty(product: DataProductOrder) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.orderQty > 0) {
                    val updatedExistingProduct = existingProduct.copy(
                        orderQty = existingProduct.orderQty + 1,
                        orderTotalPrice = existingProduct.orderPrice * (existingProduct.orderQty + 1)
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                } else {
                    enableOrder(existingProduct)
                }
            }

            _dataProducts.value = currentList
        }
    }

    fun enableOrder(product: DataProductOrder) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if(!existingProduct.isChosen){
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = true,
                        orderQty = 1,
                        orderPrice = existingProduct.standardPrice,
                        orderTotalPrice = existingProduct.standardPrice
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                }
            }

            _dataProducts.value = currentList
        }
    }

    fun disableOrder(product: DataProductOrder) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if(existingProduct.isChosen){
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = false,
                        orderQty = 0,
                        orderPrice = existingProduct.standardPrice,
                        orderTotalPrice = 0
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                }
            }

            _dataProducts.value = currentList
        }
    }

    private fun initSocket() {
        // Connect to Socket
        socketOrderHandler.connect()

        // Set up callbacks for Socket events
        socketOrderHandler.onProductsReceived = { products ->
            viewModelScope.launch {
                _dataProducts.value = products
                _loadingState.value = false
                updateCategories(products)
            }
        }

        socketOrderHandler.onNewProductReceived = { newProduct ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.add(0, newProduct)
                _dataProducts.value = currentList
                updateCategories(listOf(newProduct))
            }
        }

        socketOrderHandler.onUpdateProductReceived = { updatedProducts ->
            viewModelScope.launch {
                updateProductsWithCheck(updatedProducts)
            }
        }

        socketOrderHandler.onDeleteProductReceived = { deletedProductId ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.removeAll { it?.id == deletedProductId }
                _dataProducts.value = currentList
            }
        }

        socketOrderHandler.onErrorReceived = { error ->
            viewModelScope.launch {
                _errorMsg.value = error
            }
        }

        socketOrderHandler.onLoadingState = { it ->
            viewModelScope.launch {
                _loadingState.value = it
            }
        }

        socketOrderHandler.onErrorState = { it ->
            viewModelScope.launch {
                _errorState.value = it
            }
        }
    }

    override fun onCleared() {
        // Disconnect Socket when ViewModel is cleared
        socketOrderHandler.disconnect()
        super.onCleared()
    }
}