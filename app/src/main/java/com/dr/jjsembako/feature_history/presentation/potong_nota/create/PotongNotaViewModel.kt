package com.dr.jjsembako.feature_history.presentation.potong_nota.create

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.CanceledStore
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.SelectPNRItem
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.utils.DataMapper.mapListOrderToProductsItemToListSelectPNRItem
import com.dr.jjsembako.core.utils.DataMapper.mapSelectPNRItemToCanceledStore
import com.dr.jjsembako.feature_history.domain.usecase.canceled.HandleCreateCanceledUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PotongNotaViewModel @Inject constructor(
    private val canceledStore: DataStore<CanceledStore>,
    private val fetchOrderUseCase: FetchOrderUseCase,
    private val handleCreateCanceledUseCase: HandleCreateCanceledUseCase
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

    private val _orderData = MutableLiveData<DetailOrderData?>()
    val orderData: DetailOrderData? get() = _orderData.value

    private val _productOrder = MutableLiveData<List<SelectPNRItem?>?>()
    val productOrder: LiveData<List<SelectPNRItem?>?> get() = _productOrder

    private val _canceledData = MutableLiveData<CanceledStore>()
    val canceledData: LiveData<CanceledStore?> get() = _canceledData

    private var _id: String? = null

    init {
        viewModelScope.launch {
            _canceledData.value = getCanceledStore()
        }
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
        viewModelScope.launch {
            _canceledData.value = getCanceledStore()
        }
        fetchOrder(id)
        recoveryData()
    }

    fun reset() {
        viewModelScope.launch {
            setCanceledStore()
        }
    }

    private suspend fun getCanceledStore(): CanceledStore {
        return canceledStore.data.first()
    }

    private suspend fun setCanceledStore(data: CanceledStore? = null) {
        canceledStore.updateData {
            if (data == null) {
                CanceledStore.getDefaultInstance()
            } else {
                CanceledStore.newBuilder(data).build()
            }
        }
        _canceledData.value = canceledStore.data.first() // Update UI state
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
                        _productOrder.value =
                            mapListOrderToProductsItemToListSelectPNRItem(it.data!!.orderToProducts)
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

    fun handleCreateCanceled() {
        val id = _id ?: return
        if (canceledData.value == null) return
        else {
            val productId = canceledData.value!!.idProduct
            val amount = canceledData.value!!.amountSelected
            viewModelScope.launch {
                handleCreateCanceledUseCase.handleCreateCanceled(id, productId, amount).collect {
                    when (it) {
                        is Resource.Loading -> _stateSecond.value = StateResponse.LOADING
                        is Resource.Success -> {
                            _stateSecond.value = StateResponse.SUCCESS
                            _message.value = it.message
                            _statusCode.value = it.status
                            setCanceledStore()
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

    private fun recoveryData() {
        if (canceledData.value == null) return
        else {
            if (productOrder.value?.isEmpty() == true) return
            else {
                val currentList = _productOrder.value.orEmpty().toMutableList()
                val index = currentList.indexOfFirst { it?.id == canceledData.value!!.idProduct }
                if (index != -1) {
                    val existingProduct = currentList[index]!!
                    val updatedExistingProduct = existingProduct.copy(
                        amountSelected = canceledData.value!!.amountSelected,
                        isChosen = true
                    )
                    currentList[index] = updatedExistingProduct
                    currentList.remove(existingProduct)

                    _productOrder.value = currentList
                }
            }
        }
    }

    fun updateSelectedAmount(product: SelectPNRItem, qty: String) {
        viewModelScope.launch {
            val currentList = _productOrder.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (qty.isNotEmpty()) {
                    val amount = qty.toInt()
                    if (amount != existingProduct.amountSelected) {
                        val updatedExistingProduct = existingProduct.copy(
                            amountSelected = amount
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)
                        setCanceledStore(mapSelectPNRItemToCanceledStore(updatedExistingProduct))
                        _canceledData.value = getCanceledStore()
                        _productOrder.value = currentList
                    }
                } else {
                    disableChoose(existingProduct)
                }
            }
        }
    }

    fun minusSelectedAmount(product: SelectPNRItem) {
        viewModelScope.launch {
            val currentList = _productOrder.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.amountSelected > 0) {
                    val updatedExistingProduct = existingProduct.copy(
                        amountSelected = existingProduct.amountSelected - 1
                    )
                    if (updatedExistingProduct.amountSelected == 0) disableChoose(existingProduct)
                    else {
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)
                        setCanceledStore(mapSelectPNRItemToCanceledStore(updatedExistingProduct))
                        _canceledData.value = getCanceledStore()
                    }

                    _productOrder.value = currentList
                }
            }
        }
    }

    fun plusSelectedAmount(product: SelectPNRItem) {
        viewModelScope.launch {
            val currentList = _productOrder.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.amountSelected in 1..999) {
                    val updatedExistingProduct = existingProduct.copy(
                        amountSelected = existingProduct.amountSelected + 1
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    setCanceledStore(mapSelectPNRItemToCanceledStore(updatedExistingProduct))
                    _canceledData.value = getCanceledStore()
                    _productOrder.value = currentList
                } else {
                    enableChoose(existingProduct)
                }
            }
        }
    }

    fun enableChoose(product: SelectPNRItem) {
        viewModelScope.launch {
            val currentList = _productOrder.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (!existingProduct.isChosen) {
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = true,
                        amountSelected = 1
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    setCanceledStore(mapSelectPNRItemToCanceledStore(updatedExistingProduct))
                    _canceledData.value = getCanceledStore()
                }

                _productOrder.value = currentList
            }
        }
    }

    fun disableChoose(product: SelectPNRItem) {
        viewModelScope.launch {
            val currentList = _productOrder.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.isChosen) {
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = false,
                        amountSelected = 0
                    )
                    currentList[productIndex] = updatedExistingProduct
                    setCanceledStore()
                    _canceledData.value = getCanceledStore()
                }

                _productOrder.value = currentList
            }
        }
    }
}