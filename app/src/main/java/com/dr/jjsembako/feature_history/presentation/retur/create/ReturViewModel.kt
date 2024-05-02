package com.dr.jjsembako.feature_history.presentation.retur.create

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.ReturStore
import com.dr.jjsembako.SubstituteStore
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.SelectPNRItem
import com.dr.jjsembako.core.data.model.SubstituteProduct
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import com.dr.jjsembako.core.utils.DataMapper.mapListOrderToProductsItemToListSelectPNRItem
import com.dr.jjsembako.core.utils.DataMapper.mapSelectPNRItemToReturStore
import com.dr.jjsembako.core.utils.DataMapper.mapSelectSubstituteItemToSubstituteStore
import com.dr.jjsembako.feature_history.data.SocketReturHandler
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.retur.HandleCreateReturUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReturViewModel @Inject constructor(
    private val socketReturHandler: SocketReturHandler,
    private val returStore: DataStore<ReturStore>,
    private val substituteStore: DataStore<SubstituteStore>,
    private val fetchOrderUseCase: FetchOrderUseCase,
    private val handleCrateReturUseCase: HandleCreateReturUseCase
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

    private val _orderData = MutableLiveData<DetailOrder?>()
    val orderData: DetailOrder? get() = _orderData.value

    private val _productOrder = MutableLiveData<List<SelectPNRItem?>?>()
    private val productOrder: LiveData<List<SelectPNRItem?>?> get() = _productOrder

    private val _dataProducts = MutableLiveData<List<SubstituteProduct?>>()
    private val dataProducts: LiveData<List<SubstituteProduct?>> get() = _dataProducts

    private val _returData = MutableLiveData<ReturStore?>()
    private val returData: LiveData<ReturStore?> get() = _returData

    private val _substituteData = MutableLiveData<SubstituteStore?>()
    private val substituteData: LiveData<SubstituteStore?> get() = _substituteData

    private val _selectedDataR = MutableLiveData<SelectPNRItem?>()
    val selectedDataR: LiveData<SelectPNRItem?> get() = _selectedDataR

    private val _selectedDataS = MutableLiveData<SubstituteProduct?>()
    val selectedDataS: LiveData<SubstituteProduct?> get() = _selectedDataS

    private var _id: String? = null

    init {
        viewModelScope.launch {
            _returData.value = getReturStore()
            _substituteData.value = getSubstituteStore()
        }
        initSocket()
    }

    fun setId(id: String) {
        _id = id
        refresh()
    }

    fun setStateSecond(state: StateResponse?) {
        _stateSecond.value = state
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    fun refresh() {
        viewModelScope.launch {
            _returData.value = getReturStore()
            _substituteData.value = getSubstituteStore()
        }
        val id = _id ?: return
        fetchOrder(id)
    }

    fun reset() {
        viewModelScope.launch {
            setReturStore()
            setSubstituteStore()
            _returData.value = getReturStore()
            _substituteData.value = getSubstituteStore()
        }
        if (selectedDataR.value != null) disableChoose()
        if (selectedDataS.value != null) disableOrder()
    }

    private suspend fun getReturStore(): ReturStore {
        return returStore.data.first()
    }

    private suspend fun setReturStore(data: ReturStore? = null) {
        returStore.updateData {
            if (data == null) {
                ReturStore.getDefaultInstance()
            } else {
                ReturStore.newBuilder(data).build()
            }
        }
        _returData.value = returStore.data.first() // Update UI state
    }

    private suspend fun getSubstituteStore(): SubstituteStore {
        return substituteStore.data.first()
    }

    private suspend fun setSubstituteStore(data: SubstituteStore? = null) {
        substituteStore.updateData {
            if (data == null) {
                SubstituteStore.getDefaultInstance()
            } else {
                SubstituteStore.newBuilder(data).build()
            }
        }
        _substituteData.value = substituteStore.data.first() // Update UI state
    }

    fun fetchOrder(id: String) {
        viewModelScope.launch {
            _returData.value = getReturStore()
            _substituteData.value = getSubstituteStore()
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
                        _productOrder.value =
                            mapListOrderToProductsItemToListSelectPNRItem(it.data!!.orderToProducts)
                        recoveryData()
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

    fun handleCreateRetur() {
        val id = _id ?: return
        if (returData.value == null) return
        else {
            if (substituteData.value == null) return
            else {
                viewModelScope.launch {
                    handleCrateReturUseCase.handleCreateRetur(
                        id,
                        substituteData.value!!.id,
                        returData.value!!.idProduct,
                        returData.value!!.amountSelected,
                        substituteData.value!!.selledPrice
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
    }

    private fun recoveryData() {
        if (returData.value != null) {
            if (productOrder.value?.isEmpty() == true) return
            else {
                val currentList = _productOrder.value.orEmpty().toMutableList()
                val index = currentList.indexOfFirst { it?.id == returData.value!!.id }
                if (index != -1) {
                    val existingProduct = currentList[index]!!
                    val updatedExistingProduct = existingProduct.copy(
                        amountSelected = returData.value!!.amountSelected,
                        isChosen = true
                    )
                    currentList[index] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    _selectedDataR.value = updatedExistingProduct
                    _productOrder.value = currentList
                }
            }
        }
        if (substituteData.value != null) {
            if (dataProducts.value?.isEmpty() == true) return
            else {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                val index = currentList.indexOfFirst { it?.id == substituteData.value!!.id }
                if (index != -1) {
                    val existingProduct = currentList[index]!!
                    val updatedExistingProduct = existingProduct.copy(
                        selledPrice = substituteData.value!!.selledPrice,
                        isChosen = true
                    )
                    currentList[index] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    _selectedDataS.value = updatedExistingProduct
                    _dataProducts.value = currentList
                }
            }
        }
    }

    fun updateSelectedAmount(product: SelectPNRItem, qty: String) {
        viewModelScope.launch {
            if (qty.isNotEmpty()) {
                val updateData = product.copy(
                    amountSelected = qty.toInt()
                )
                setReturStore(mapSelectPNRItemToReturStore(updateData))
                _returData.value = getReturStore()
                _selectedDataR.value = updateData
            } else disableChoose()
        }
    }

    fun minusSelectedAmount(product: SelectPNRItem) {
        viewModelScope.launch {
            if (product.amountSelected > 0) {
                val updateData = product.copy(
                    amountSelected = product.amountSelected - 1
                )
                setReturStore(mapSelectPNRItemToReturStore(updateData))
                _returData.value = getReturStore()
                _selectedDataR.value = updateData
            } else disableChoose()
        }
    }

    fun plusSelectedAmount(product: SelectPNRItem) {
        viewModelScope.launch {
            if (product.amountSelected in 1..999) {
                val updateData = product.copy(
                    amountSelected = product.amountSelected + 1
                )
                setReturStore(mapSelectPNRItemToReturStore(updateData))
                _returData.value = getReturStore()
                _selectedDataR.value = updateData
            } else enableChoose(product)
        }
    }

    fun enableChoose(product: SelectPNRItem) {
        viewModelScope.launch {
            val updateData = product.copy(
                isChosen = true,
                amountSelected = 1
            )
            setReturStore(mapSelectPNRItemToReturStore(updateData))
            _returData.value = getReturStore()
            _selectedDataR.value = updateData
        }
    }

    fun disableChoose() {
        viewModelScope.launch {
            setReturStore()
            _returData.value = getReturStore()
            _selectedDataR.value = null
        }
    }

    private fun updateProductsWithCheck(updatedProducts: List<SubstituteProduct>) {
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
                            selledPrice = existingProduct.selledPrice,
                            isChosen = true
                        )
                        setSubstituteStore(
                            mapSelectSubstituteItemToSubstituteStore(updatedExistingProduct)
                        )
                        _substituteData.value = getSubstituteStore()
                        _selectedDataS.value = updatedExistingProduct
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
                            selledPrice = 0,
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

    fun updateOrderPrice(product: SubstituteProduct, price: String) {
        viewModelScope.launch {
            if (price.isNotEmpty()) {
                val updateData = product.copy(
                    selledPrice = price.toLong()
                )
                setSubstituteStore(mapSelectSubstituteItemToSubstituteStore(updateData))
                _substituteData.value = getSubstituteStore()
                _selectedDataS.value = updateData
            } else disableOrder()
        }
    }

    fun enableOrder(product: SubstituteProduct) {
        viewModelScope.launch {
            val updateData = product.copy(
                isChosen = true,
                selledPrice = product.selledPrice
            )
            setSubstituteStore(mapSelectSubstituteItemToSubstituteStore(updateData))
            _substituteData.value = getSubstituteStore()
            _selectedDataS.value = updateData
        }
    }

    fun disableOrder() {
        viewModelScope.launch {
            setSubstituteStore()
            _substituteData.value = getSubstituteStore()
            _selectedDataS.value = null
        }
    }

    private fun initSocket() {
        // Connect to Socket
        socketReturHandler.connect()

        // Set up callbacks for Socket events
        socketReturHandler.onProductsReceived = { products ->
            viewModelScope.launch {
                _dataProducts.value = products
                _loadingState.value = false
                recoveryData()
            }
        }

        socketReturHandler.onNewProductReceived = { newProduct ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.add(0, newProduct)
                _dataProducts.value = currentList
            }
        }

        socketReturHandler.onUpdateProductReceived = { updatedProducts ->
            viewModelScope.launch {
                updateProductsWithCheck(updatedProducts)
            }
        }

        socketReturHandler.onDeleteProductReceived = { deletedProductId ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.removeAll { it?.id == deletedProductId }
                _dataProducts.value = currentList
            }
        }

        socketReturHandler.onErrorReceived = { error ->
            viewModelScope.launch {
                _errorMsg.value = error
            }
        }

        socketReturHandler.onLoadingState = { it ->
            viewModelScope.launch {
                _loadingState.value = it
            }
        }

        socketReturHandler.onErrorState = { it ->
            viewModelScope.launch {
                _errorState.value = it
            }
        }
    }

    override fun onCleared() {
        // Disconnect Socket when ViewModel is cleared
        socketReturHandler.disconnect()
        super.onCleared()
    }
}