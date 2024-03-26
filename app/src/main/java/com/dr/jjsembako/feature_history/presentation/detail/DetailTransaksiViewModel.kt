package com.dr.jjsembako.feature_history.presentation.detail

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.CanceledStore
import com.dr.jjsembako.ReturStore
import com.dr.jjsembako.SubstituteStore
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.order.DataDetailOrder
import com.dr.jjsembako.feature_history.domain.model.UpdateStateOrder
import com.dr.jjsembako.feature_history.domain.usecase.canceled.HandleDeleteCanceledUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleDeleteOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleDeleteProductOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleUpdatePaymentStatusUseCase
import com.dr.jjsembako.feature_history.domain.usecase.retur.HandleDeleteReturUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTransaksiViewModel @Inject constructor(
    private val canceledStore: DataStore<CanceledStore>,
    private val returStore: DataStore<ReturStore>,
    private val substituteStore: DataStore<SubstituteStore>,
    private val fetchOrderUseCase: FetchOrderUseCase,
    private val handleUpdatePaymentStatusUseCase: HandleUpdatePaymentStatusUseCase,
    private val handleDeleteProductOrderUseCase: HandleDeleteProductOrderUseCase,
    private val handleDeleteOrderUseCase: HandleDeleteOrderUseCase,
    private val handleDeleteCanceledUseCase: HandleDeleteCanceledUseCase,
    private val handleDeleteReturUseCase: HandleDeleteReturUseCase
) : ViewModel() {

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _stateUpdate = MutableLiveData<UpdateStateOrder?>()
    val stateUpdate: LiveData<UpdateStateOrder?> = _stateUpdate

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _orderData = MutableLiveData<DataDetailOrder?>()
    val orderData: LiveData<DataDetailOrder?> get() = _orderData

    private var _id: String? = null

    init {
        viewModelScope.launch {
            resetDataStorePNR()
        }
    }

    fun setId(id: String) {
        _id = id
        init()
    }

    fun setStateSecond(state: StateResponse?) {
        _stateSecond.value = state
    }

    fun setStateUpdate(state: UpdateStateOrder?) {
        _stateUpdate.value = state
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    private suspend fun resetDataStorePNR() {
        returStore.updateData { ReturStore.getDefaultInstance() }
        canceledStore.updateData { CanceledStore.getDefaultInstance() }
        substituteStore.updateData { SubstituteStore.getDefaultInstance() }
    }

    private fun init() {
        refresh()
    }

    fun refresh() {
        val id = _id ?: return
        fetchOrder(id)
    }

    fun fetchOrder(id: String) {
        viewModelScope.launch {
            fetchOrderUseCase.fetchOrder(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (orderData.value?.id.isNullOrEmpty()) _stateFirst.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (orderData.value?.id.isNullOrEmpty()) _stateFirst.value =
                            StateResponse.SUCCESS
                        else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _orderData.value = it.data
                    }

                    is Resource.Error -> {
                        if (orderData.value?.id.isNullOrEmpty()) _stateFirst.value =
                            StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    fun handleUpdatePaymentStatus() {
        val id = _id ?: return
        viewModelScope.launch {
            handleUpdatePaymentStatusUseCase.handleUpdatePaymentStatus(id).collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING

                    is Resource.Success -> {
                        _stateSecond.value = StateResponse.SUCCESS
                        _stateUpdate.value = UpdateStateOrder.PAYMENT
                        _message.value = it.message
                        _statusCode.value = it.status
                        val oldValue = orderData
                        val newValue = oldValue.value!!.copy(
                            paymentStatus = 1
                        )
                        _orderData.value = newValue
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

    fun handleDeleteProductOrder(productId: String) {
        val id = _id ?: return
        viewModelScope.launch {
            handleDeleteProductOrderUseCase.handleDeleteProductOrder(id, productId).collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING

                    is Resource.Success -> {
                        _stateSecond.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        val oldValue = orderData.value
                        val deletedProduct = oldValue!!.orderToProducts.first { data ->
                            data.product.id == productId
                        }
                        val newTotalPrice = oldValue.totalPrice - (deletedProduct.selledPrice * deletedProduct.actualAmount)
                        val newOrderToProducts = oldValue.orderToProducts.filter { data ->
                            data.product.id != productId
                        }
                        val newValue = oldValue.copy(
                            orderToProducts = newOrderToProducts,
                            totalPrice = newTotalPrice,
                            actualTotalPrice = newTotalPrice
                        )
                        _orderData.value = newValue
                        if (orderData.value!!.orderToProducts.isEmpty()) {
                            _stateUpdate.value = UpdateStateOrder.DEL_ORDER
                        } else {
                            _stateUpdate.value = UpdateStateOrder.DEL_PRODUCT
                        }
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

    fun handleDeleteOrder() {
        val id = _id ?: return
        viewModelScope.launch {
            handleDeleteOrderUseCase.handleDeleteOrder(id).collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING

                    is Resource.Success -> {
                        _stateSecond.value = StateResponse.SUCCESS
                        _stateUpdate.value = UpdateStateOrder.DEL_ORDER
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

    fun handleDeleteCanceled(canceledId: String) {
        viewModelScope.launch {
            handleDeleteCanceledUseCase.handleDeleteCanceled(canceledId).collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING

                    is Resource.Success -> {
                        _stateSecond.value = StateResponse.SUCCESS
                        _stateUpdate.value = UpdateStateOrder.DEL_CANCELED
                        _message.value = it.message
                        _statusCode.value = it.status
                        val oldValue = orderData.value
                        if (oldValue?.canceled != null) {
                            val newCanceled = oldValue.canceled.filter { data ->
                                data!!.id != canceledId
                            }
                            val delCanceled = oldValue.canceled.first { data ->
                                data!!.id == canceledId
                            }
                            val newValue = oldValue.copy(
                                canceled = newCanceled,
                                actualTotalPrice = oldValue.actualTotalPrice + ((delCanceled?.selledPrice
                                    ?: 0L) * (delCanceled?.amount ?: 0))
                            )
                            _orderData.value = newValue
                        }
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

    fun handleDeleteRetur(returId: String) {
        viewModelScope.launch {
            handleDeleteReturUseCase.handleDeleteRetur(returId).collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING

                    is Resource.Success -> {
                        _stateSecond.value = StateResponse.SUCCESS
                        _stateUpdate.value = UpdateStateOrder.DEL_RETUR
                        _message.value = it.message
                        _statusCode.value = it.status
                        val oldValue = orderData.value
                        if (oldValue?.retur != null) {
                            val newRetur = oldValue.retur.filter { data ->
                                data!!.id != returId
                            }
                            val delRetur = oldValue.retur.first { data ->
                                data!!.id == returId
                            }
                            val newValue = oldValue.copy(
                                retur = newRetur,
                                actualTotalPrice = oldValue.actualTotalPrice +
                                        (((delRetur?.oldSelledPrice ?: 0L) - (delRetur?.selledPrice
                                            ?: 0L)) * (delRetur?.amount ?: 0))
                            )
                            _orderData.value = newValue
                        }
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