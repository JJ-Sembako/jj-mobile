package com.dr.jjsembako.feature_history.presentation.edit_product_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import com.dr.jjsembako.core.utils.DataMapper
import com.dr.jjsembako.feature_history.data.SocketModifyOrderHandler
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleUpdateProductOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBarangPesananViewModel @Inject constructor(
    private val socketModifyOrderHandler: SocketModifyOrderHandler,
    private val fetchOrderUseCase: FetchOrderUseCase,
    private val handleUpdateProductOrderUseCase: HandleUpdateProductOrderUseCase
) : ViewModel() {

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData(false)
    val errorState: LiveData<Boolean> get() = _errorState

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    private val _dataRawCategories = MutableLiveData<List<String?>>()
    private val dataRawCategories: LiveData<List<String?>> get() = _dataRawCategories

    private val _dataCategories = MutableLiveData<List<FilterOption?>>()
    val dataCategories: LiveData<List<FilterOption?>> get() = _dataCategories

    private val _dataProducts = MutableLiveData<List<DataProductOrder?>>()
    val dataProducts: LiveData<List<DataProductOrder?>> get() = _dataProducts

    private val _orderData = MutableLiveData<DetailOrder?>()
    val orderData: DetailOrder? get() = _orderData.value

    private val _selectedData = MutableLiveData<DataProductOrder?>()
    val selectedData: LiveData<DataProductOrder?> get() = _selectedData

    private var _id: String? = null

    init {
        initSocket()
    }

    fun setId(id: String) {
        _id = id
        init()
    }

    fun setStateSecond(state: StateResponse?) {
        _stateSecond.value = state
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    private fun init() {
        refresh()
    }

    fun refresh() {
        val id = _id ?: return
        fetchOrder(id)
    }

    fun reset() {
        if (selectedData.value == null) return
        else disableOrder(selectedData.value!!)
    }

    fun fetchOrder(id: String) {
        viewModelScope.launch {
            fetchOrderUseCase.fetchOrder(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (orderData?.id.isNullOrEmpty()) _stateFirst.value = StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (orderData?.id.isNullOrEmpty()) _stateFirst.value = StateResponse.SUCCESS
                        else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _orderData.value = it.data
                    }

                    is Resource.Error -> {
                        if (orderData?.id.isNullOrEmpty()) _stateFirst.value = StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    fun handleUpdateProductOrder() {
        val id = _id ?: return
        if (selectedData.value == null) return
        else {
            val productId = selectedData.value!!.id
            val amountInUnit = selectedData.value!!.orderQty
            val pricePerUnit = selectedData.value!!.orderPrice
            viewModelScope.launch {
                handleUpdateProductOrderUseCase.handleUpdateProductOrder(
                    id, productId, amountInUnit, pricePerUnit
                ).collect {
                    when (it) {
                        is Resource.Loading -> {
                            _stateSecond.value = StateResponse.LOADING
                        }

                        is Resource.Success -> {
                            _stateSecond.value = StateResponse.SUCCESS
                            _message.value = it.message
                            _statusCode.value = it.status
                        }

                        is Resource.Error -> {
                            _stateSecond.value = StateResponse.ERROR
                            _message.value = it.message
                            _statusCode.value = it.status
                        }
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
                        _selectedData.value = updatedExistingProduct
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
                    if (orderPrice != existingProduct.orderPrice) {
                        val updatedExistingProduct = existingProduct.copy(
                            orderPrice = orderPrice,
                            orderTotalPrice = existingProduct.orderQty * orderPrice
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)
                        _selectedData.value = updatedExistingProduct
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
                    if (orderQty != existingProduct.orderQty) {
                        val updatedExistingProduct = existingProduct.copy(
                            orderQty = orderQty,
                            orderTotalPrice = existingProduct.orderPrice * orderQty
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)
                        _selectedData.value = updatedExistingProduct
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
                        _selectedData.value = updatedExistingProduct
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
                    _selectedData.value = updatedExistingProduct
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
            val orderInfo = orderData?.orderToProducts?.find { it.product.id == product.id }

            if (productIndex != -1 && orderInfo != null) {
                val existingProduct = currentList[productIndex]!!

                if (!existingProduct.isChosen) {
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = true,
                        orderQty = orderInfo.amount,
                        orderPrice = orderInfo.selledPrice,
                        orderTotalPrice = orderInfo.amount * orderInfo.selledPrice
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    _selectedData.value = updatedExistingProduct
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

                if (existingProduct.isChosen) {
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = false,
                        orderQty = 0,
                        orderPrice = existingProduct.standardPrice,
                        orderTotalPrice = 0
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    _selectedData.value = null
                }

                _dataProducts.value = currentList
            }
        }
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

    private fun recoveryData() {
        if (selectedData.value == null) return
        else {
            if (dataProducts.value?.isEmpty() == true) return
            else {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                val index = currentList.indexOfFirst { it?.id == selectedData.value!!.id }
                if (index != -1) {
                    val existingProduct = currentList[index]!!
                    val updatedExistingProduct = existingProduct.copy(
                        orderQty = selectedData.value!!.orderQty,
                        orderPrice = selectedData.value!!.orderPrice,
                        orderTotalPrice = selectedData.value!!.orderTotalPrice,
                        isChosen = true
                    )
                    currentList[index] = updatedExistingProduct
                    currentList.remove(existingProduct)

                    _dataProducts.value = currentList
                }
            }
        }
    }

    private fun initSocket() {
        // Connect to Socket
        socketModifyOrderHandler.connect()

        // Set up callbacks for Socket events
        socketModifyOrderHandler.onProductsReceived = { products ->
            viewModelScope.launch {
                _dataProducts.value = products
                _loadingState.value = false
                updateCategories(products)
                recoveryData()
            }
        }

        socketModifyOrderHandler.onNewProductReceived = { newProduct ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.add(0, newProduct)
                _dataProducts.value = currentList
                updateCategories(listOf(newProduct))
            }
        }

        socketModifyOrderHandler.onUpdateProductReceived = { updatedProducts ->
            viewModelScope.launch {
                updateProductsWithCheck(updatedProducts)
            }
        }

        socketModifyOrderHandler.onDeleteProductReceived = { deletedProductId ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.removeAll { it?.id == deletedProductId }
                _dataProducts.value = currentList
            }
        }

        socketModifyOrderHandler.onErrorReceived = { error ->
            viewModelScope.launch {
                _errorMsg.value = error
            }
        }

        socketModifyOrderHandler.onLoadingState = { it ->
            viewModelScope.launch {
                _loadingState.value = it
            }
        }

        socketModifyOrderHandler.onErrorState = { it ->
            viewModelScope.launch {
                _errorState.value = it
            }
        }
    }

    override fun onCleared() {
        // Disconnect Socket when ViewModel is cleared
        socketModifyOrderHandler.disconnect()
        super.onCleared()
    }

}