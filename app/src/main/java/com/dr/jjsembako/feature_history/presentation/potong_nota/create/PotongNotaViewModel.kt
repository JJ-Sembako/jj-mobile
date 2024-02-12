package com.dr.jjsembako.feature_history.presentation.potong_nota.create

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.CanceledStore
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
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

    private val _selectedData = MutableLiveData<CanceledStore>()
    val selectedData: LiveData<CanceledStore?> get() = _selectedData

    private var _id: String? = null

    init {
        viewModelScope.launch {
            _selectedData.value = getCanceledStore()
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
        fetchOrder(id)
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
        _selectedData.value = canceledStore.data.first() // Update UI state
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
}