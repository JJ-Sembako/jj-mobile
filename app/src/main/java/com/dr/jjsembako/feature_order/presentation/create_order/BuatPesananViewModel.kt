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
import com.dr.jjsembako.core.data.model.PreferencesKeys
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
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
    private val fetchDetailSelectedCustUseCase: FetchDetailSelectedCustUseCase
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

    private val _selectedCustomer = MutableLiveData<DataCustomer?>()
    val selectedCustomer: LiveData<DataCustomer?> get() = _selectedCustomer

    private val _idCustomer = MutableStateFlow("")
    val idCustomer: StateFlow<String> = _idCustomer

    private val _payment = MutableStateFlow(0)
    val payment: StateFlow<Int> = _payment

    private val _productsList = MutableStateFlow(ProductOrderList.getDefaultInstance())
    val productsList: StateFlow<ProductOrderList> = _productsList

    init {
        viewModelScope.launch {
            _idCustomer.value = getIdCustomer()
            _payment.value = getPayment()
        }
    }

    fun reset() {
        viewModelScope.launch {
            setIdCustomer("")
            setPayment(0)
            _selectedCustomer.value = null
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _idCustomer.value = getIdCustomer()
            _payment.value = getPayment()

            if (idCustomer.value.isNotEmpty()) fetchDetailCustomer(idCustomer.value)
        }
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    private suspend fun getIdCustomer(): String {
        return preferencesDataStore.data.first()[PreferencesKeys.ID_CUSTOMER] ?: ""
    }

    private suspend fun getPayment(): Int {
        return preferencesDataStore.data.first()[PreferencesKeys.PAYMENT] ?: 0
    }

    private suspend fun getProductsList(): ProductOrderList {
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

    suspend fun setProductsList(productsList: List<ProductOrderStore>) {
        productsDataStore.updateData {
            if (productsList.isEmpty()) {
                ProductOrderList.getDefaultInstance() // Set empty list
            } else {
                ProductOrderList.newBuilder().addAllData(productsList).build()
            }
        }
        _productsList.value = productsDataStore.data.first() // Update UI state
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
}