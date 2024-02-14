package com.dr.jjsembako.feature_history.presentation.retur.select_product

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.ReturStore
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.model.SelectPNRItem
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.data.remote.response.order.OrderToProductsItem
import com.dr.jjsembako.core.utils.DataMapper.mapListDataCategoryToListFilterOption
import com.dr.jjsembako.core.utils.DataMapper.mapListOrderToProductsItemToListSelectPNRItem
import com.dr.jjsembako.core.utils.DataMapper.mapSelectPNRItemToReturStore
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PBReturViewModel @Inject constructor(
    private val returStore: DataStore<ReturStore>,
    private val fetchOrderUseCase: FetchOrderUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _dataRawCategories = MutableLiveData<List<String?>>()
    private val dataRawCategories: LiveData<List<String?>> get() = _dataRawCategories

    private val _dataCategories = MutableLiveData<List<FilterOption?>>()
    val dataCategories: LiveData<List<FilterOption?>> get() = _dataCategories

    private val _orderData = MutableLiveData<DetailOrderData?>()
    val orderData: DetailOrderData? get() = _orderData.value

    private val _productOrder = MutableLiveData<List<SelectPNRItem?>?>()
    val productOrder: LiveData<List<SelectPNRItem?>?> get() = _productOrder

    private val _selectedData = MutableLiveData<SelectPNRItem?>()
    val selectedData: LiveData<SelectPNRItem?> get() = _selectedData

    private val _returData = MutableLiveData<ReturStore?>()
    val returData: LiveData<ReturStore?> get() = _returData

    private var _id: String? = null

    init {
        viewModelScope.launch {
            _returData.value = getReturStore()
        }
    }

    fun setId(id: String) {
        _id = id
        init()
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    private fun init() {
        refresh()
        updateCategories(orderData?.orderToProducts.orEmpty())
    }

    fun refresh() {
        val id = _id ?: return
        viewModelScope.launch {
            _returData.value = getReturStore()
        }
        fetchOrder(id)
        recoveryData()
    }

    fun saveData() {
        if (returData.value == null) return
        else {
            viewModelScope.launch {
                setReturStore(returData.value)
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            setReturStore()
            _returData.value = getReturStore()
        }
        if (selectedData.value != null) return
        else disableChoose(selectedData.value!!)
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

    fun fetchOrder(id: String) {
        viewModelScope.launch {
            fetchOrderUseCase.fetchOrder(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (orderData?.id.isNullOrEmpty()) _state.value = StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (orderData?.id.isNullOrEmpty()) _state.value = StateResponse.SUCCESS
                        else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _orderData.value = it.data
                        _productOrder.value =
                            mapListOrderToProductsItemToListSelectPNRItem(it.data!!.orderToProducts)
                    }

                    is Resource.Error -> {
                        if (orderData?.id.isNullOrEmpty()) _state.value = StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    private fun recoveryData() {
        if (returData.value == null) return
        else {
            if (productOrder.value?.isEmpty() == true) return
            else {
                val currentList = _productOrder.value.orEmpty().toMutableList()
                val index = currentList.indexOfFirst { it?.id == returData.value!!.idProduct }
                if (index != -1) {
                    val existingProduct = currentList[index]!!
                    val updatedExistingProduct = existingProduct.copy(
                        amountSelected = returData.value!!.amountSelected,
                        isChosen = true
                    )
                    currentList[index] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    _selectedData.value = updatedExistingProduct
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
                        setReturStore(mapSelectPNRItemToReturStore(updatedExistingProduct))
                        _returData.value = getReturStore()
                        _selectedData.value = updatedExistingProduct
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
                        setReturStore(mapSelectPNRItemToReturStore(updatedExistingProduct))
                        _returData.value = getReturStore()
                        _selectedData.value = updatedExistingProduct
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
                    setReturStore(mapSelectPNRItemToReturStore(updatedExistingProduct))
                    _returData.value = getReturStore()
                    _selectedData.value = updatedExistingProduct
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
                    setReturStore(mapSelectPNRItemToReturStore(updatedExistingProduct))
                    _returData.value = getReturStore()
                    _selectedData.value = updatedExistingProduct
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
                    setReturStore()
                    _returData.value = getReturStore()
                    _selectedData.value = null
                }

                _productOrder.value = currentList
            }
        }
    }

    private fun updateCategories(newProducts: List<OrderToProductsItem?>) {
        viewModelScope.launch {
            val newCategories = newProducts.mapNotNull { it?.product?.category }.distinct()
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
}