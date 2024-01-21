package com.dr.jjsembako.feature_order.presentation.select_product

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.ProductOrderList
import com.dr.jjsembako.ProductOrderStore
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.utils.DataMapper
import com.dr.jjsembako.feature_order.data.SocketOrderHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihBarangViewModel @Inject constructor(
    private val productsDataStore: DataStore<ProductOrderList>,
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

    private val _orderList = MutableStateFlow(ProductOrderList.getDefaultInstance())
    val orderList: StateFlow<ProductOrderList> = _orderList

    init {
        initSocket()
        viewModelScope.launch {
            _orderList.value = getProductsList()
        }
    }

    private suspend fun recoveryOrderData() {
        val currentList = _dataProducts.value.orEmpty().toMutableList()
        val currentOrderList = _orderList.value.dataList

        if (currentOrderList.isNotEmpty() && currentList.isNotEmpty()) {
            for (orderItem in currentOrderList) {
                val index = currentList.indexOfFirst { it?.id == orderItem.id }
                if (index != -1) {
                    val existingProduct = currentList[index]!!
                    if(existingProduct.amountPerUnit != 0){
                        val updatedExistingProduct = existingProduct.copy(
                            orderQty = orderItem.orderQty,
                            orderPrice = orderItem.orderPrice,
                            orderTotalPrice = orderItem.orderTotalPrice,
                            isChosen = true
                        )
                        currentList[index] = updatedExistingProduct
                        currentList.remove(existingProduct)

                        _dataProducts.value = currentList
                    }
                }
            }
        }
    }

    suspend fun saveData() {
        val currentList = _dataProducts.value.orEmpty().toMutableList()
        if(currentList.isEmpty()){
            setProductsList()
        } else {
            val selectedProduct = currentList.filter { product -> product!!.isChosen }
        }
    }

    private suspend fun getProductsList(): ProductOrderList {
        return productsDataStore.data.first()
    }

    private suspend fun setProductsList(orderList: List<ProductOrderStore> = emptyList()) {
        productsDataStore.updateData {
            if(orderList.isEmpty()){
                ProductOrderList.getDefaultInstance()
            } else {
                ProductOrderList.newBuilder().addAllData(orderList).build()
            }
        }
        _orderList.value = productsDataStore.data.first() // Update UI state
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

                    if(existingProduct.isChosen){
                        val updatedExistingProduct = existingProduct.copy(
                            name = updatedProduct.name,
                            image = updatedProduct.image,
                            category = updatedProduct.category,
                            unit = updatedProduct.unit,
                            standardPrice = updatedProduct.standardPrice,
                            amountPerUnit = updatedProduct.amountPerUnit,
                            stockInPcs = updatedProduct.stockInPcs,
                            stockInUnit = updatedProduct.stockInUnit,
                            stockInPcsRemaining = updatedProduct.stockInPcsRemaining,
                            orderQty = existingProduct.orderQty,
                            orderPrice = existingProduct.orderPrice,
                            orderTotalPrice = existingProduct.orderTotalPrice,
                            isChosen = true
                        )

                        currentList[index] = updatedExistingProduct
                        currentList.remove(existingProduct)
                    } else {
                        val updatedExistingProduct = existingProduct.copy(
                            name = updatedProduct.name,
                            image = updatedProduct.image,
                            category = updatedProduct.category,
                            unit = updatedProduct.unit,
                            standardPrice = updatedProduct.standardPrice,
                            amountPerUnit = updatedProduct.amountPerUnit,
                            stockInPcs = updatedProduct.stockInPcs,
                            stockInUnit = updatedProduct.stockInUnit,
                            stockInPcsRemaining = updatedProduct.stockInPcsRemaining,
                            orderQty = 0,
                            orderPrice = updatedProduct.standardPrice,
                            orderTotalPrice = 0,
                            isChosen = false
                        )

                        currentList[index] = updatedExistingProduct
                        currentList.remove(existingProduct)
                    }
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
                    if(orderTotalPrice != existingProduct.orderTotalPrice){
                        val updatedExistingProduct = existingProduct.copy(
                            orderTotalPrice = orderTotalPrice,
                            orderPrice = orderTotalPrice / existingProduct.orderQty
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)

                        _dataProducts.value = currentList
                    }
                } else {
                    disableOrder(existingProduct)
                }
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
                    if(orderPrice != existingProduct.orderPrice){
                        val updatedExistingProduct = existingProduct.copy(
                            orderPrice = orderPrice,
                            orderTotalPrice = existingProduct.orderQty * orderPrice
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)

                        _dataProducts.value = currentList
                    }
                } else {
                    disableOrder(existingProduct)
                }
            }
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
                    if(orderQty != existingProduct.orderQty){
                        val updatedExistingProduct = existingProduct.copy(
                            orderQty = orderQty,
                            orderTotalPrice = existingProduct.orderPrice * orderQty
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)

                        _dataProducts.value = currentList
                    }
                } else {
                    disableOrder(existingProduct)
                }
            }
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

                    _dataProducts.value = currentList
                }
            }
        }
    }

    fun plusOrderQty(product: DataProductOrder) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.orderQty in 1..999) {
                    val updatedExistingProduct = existingProduct.copy(
                        orderQty = existingProduct.orderQty + 1,
                        orderTotalPrice = existingProduct.orderPrice * (existingProduct.orderQty + 1)
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)

                    _dataProducts.value = currentList
                } else {
                    enableOrder(existingProduct)
                }
            }
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

                _dataProducts.value = currentList
            }
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

                _dataProducts.value = currentList
            }
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