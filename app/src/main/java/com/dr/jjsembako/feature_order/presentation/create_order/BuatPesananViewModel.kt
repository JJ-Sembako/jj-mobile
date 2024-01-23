package com.dr.jjsembako.feature_order.presentation.create_order

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.ProductOrderList
import com.dr.jjsembako.ProductOrderStore
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.PreferencesKeys
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.utils.DataMapper
import com.dr.jjsembako.feature_order.data.SocketOrderHandler
import com.dr.jjsembako.feature_order.domain.usecase.FetchDetailSelectedCustUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuatPesananViewModel @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    private val productsDataStore: DataStore<ProductOrderList>,
    private val socketOrderHandler: SocketOrderHandler,
    private val fetchDetailSelectedCustUseCase: FetchDetailSelectedCustUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData(false)
    val errorState: LiveData<Boolean> get() = _errorState

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _selectedCustomer = MutableLiveData<DataCustomer?>()
    val selectedCustomer: LiveData<DataCustomer?> get() = _selectedCustomer

    private val _idCustomer = MutableStateFlow("")
    val idCustomer: StateFlow<String> = _idCustomer

    private val _payment = MutableStateFlow(0)
    val payment: StateFlow<Int> = _payment

    private val _orderList = MutableStateFlow(ProductOrderList.getDefaultInstance())
    private val orderList: StateFlow<ProductOrderList> = _orderList

    private val _dataProducts = MutableLiveData<List<DataProductOrder?>>()
    val dataProducts: LiveData<List<DataProductOrder?>> get() = _dataProducts

    init {
        initSocket()
        viewModelScope.launch {
            _idCustomer.value = getIdCustomer()
            _payment.value = getPayment()
            _orderList.value = getProductOrderList()
        }
    }

    fun reset() {
        viewModelScope.launch {
            setIdCustomer("")
            setPayment(0)
            setProductOrderList()
            _selectedCustomer.value = null
            _dataProducts.value = emptyList()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _idCustomer.value = getIdCustomer()
            _payment.value = getPayment()
            _orderList.value = getProductOrderList()

            if (idCustomer.value.isNotEmpty()) fetchDetailCustomer(idCustomer.value)
        }
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    private suspend fun saveProductOrderData() {
        val currentList = _dataProducts.value.orEmpty().toMutableList()
        if (currentList.isEmpty()) {
            setProductOrderList()
        } else {
            val selectedProduct = currentList.filter { product -> product!!.isChosen }
            setProductOrderList(
                DataMapper.mapListDataProductOrderToListProductOrderStore(
                    selectedProduct
                )
            )
        }
    }

    private suspend fun getIdCustomer(): String {
        return preferencesDataStore.data.first()[PreferencesKeys.ID_CUSTOMER] ?: ""
    }

    private suspend fun getPayment(): Int {
        return preferencesDataStore.data.first()[PreferencesKeys.PAYMENT] ?: 0
    }

    private suspend fun getProductOrderList(): ProductOrderList {
        return productsDataStore.data.first()
    }

    private suspend fun setIdCustomer(idCustomer: String) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.ID_CUSTOMER] = idCustomer
        }
        _idCustomer.value = idCustomer
    }

    suspend fun setPayment(payment: Int) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.PAYMENT] = payment
        }
        _payment.value = payment
    }

    private suspend fun setProductOrderList(productsList: List<ProductOrderStore> = emptyList()) {
        productsDataStore.updateData {
            if (productsList.isEmpty()) {
                ProductOrderList.getDefaultInstance() // Set empty list
            } else {
                ProductOrderList.newBuilder().addAllData(productsList).build()
            }
        }
        _orderList.value = productsDataStore.data.first() // Update UI state
    }

    fun fetchDetailCustomer(id: String) {
        viewModelScope.launch {
            fetchDetailSelectedCustUseCase.fetchDetailCustomer(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (selectedCustomer.value?.id.isNullOrEmpty()) _state.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (selectedCustomer.value?.id.isNullOrEmpty()) _state.value =
                            StateResponse.SUCCESS
                        else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _selectedCustomer.value = it.data
                    }

                    is Resource.Error -> {
                        if (selectedCustomer.value?.id.isNullOrEmpty()) _state.value =
                            StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                        if (statusCode.value!! in intArrayOf(400, 401)) {
                            _selectedCustomer.value = null
                            setIdCustomer("")
                        }
                    }
                }
            }
            _isRefreshing.emit(false)
        }
    }

    private fun recoveryOrderData() {
        viewModelScope.launch {
            if (orderList.value.dataList.isEmpty()) {
                _orderList.value = getProductOrderList()
            }
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val currentOrderList = orderList.value.dataList

            if (currentOrderList.isNotEmpty() && currentList.isNotEmpty()) {
                for (orderItem in currentOrderList) {
                    val index = currentList.indexOfFirst { it?.id == orderItem.id }
                    if (index != -1) {
                        val existingProduct = currentList[index]!!
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

    private fun updateProductsWithCheck(updatedProducts: List<DataProductOrder>) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()

            for (updatedProduct in updatedProducts) {
                val index = currentList.indexOfFirst { it?.id == updatedProduct.id }

                if (index != -1) {
                    val existingProduct = currentList[index]!!

                    if (existingProduct.isChosen) {
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
            saveProductOrderData()
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
                    if (orderTotalPrice != existingProduct.orderTotalPrice) {
                        val updatedExistingProduct = existingProduct.copy(
                            orderTotalPrice = orderTotalPrice,
                            orderPrice = orderTotalPrice / existingProduct.orderQty
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)

                        _dataProducts.value = currentList
                        saveProductOrderData()
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
                    if (orderPrice != existingProduct.orderPrice) {
                        val updatedExistingProduct = existingProduct.copy(
                            orderPrice = orderPrice,
                            orderTotalPrice = existingProduct.orderQty * orderPrice
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)

                        _dataProducts.value = currentList
                        saveProductOrderData()
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
                    if (orderQty != existingProduct.orderQty) {
                        val updatedExistingProduct = existingProduct.copy(
                            orderQty = orderQty,
                            orderTotalPrice = existingProduct.orderPrice * orderQty
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)

                        _dataProducts.value = currentList
                        saveProductOrderData()
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
                    saveProductOrderData()
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
                    saveProductOrderData()
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

                if (!existingProduct.isChosen) {
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
                saveProductOrderData()
            }
        }
    }

    fun disableOrder(product: DataProductOrder) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.isChosen) {
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
                saveProductOrderData()
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
                recoveryOrderData()
            }
        }

        socketOrderHandler.onNewProductReceived = { newProduct ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.add(0, newProduct)
                _dataProducts.value = currentList
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
                if ((dataProducts.value?.size ?: 0) > 0
                    && (orderList.value.dataList?.size ?: 0) > 0
                ) {
                    saveProductOrderData()
                }
                _errorState.value = it
                _orderList.value = getProductOrderList()
            }
        }
    }

    override fun onCleared() {
        // Disconnect Socket when ViewModel is cleared
        socketOrderHandler.disconnect()
        super.onCleared()
    }
}