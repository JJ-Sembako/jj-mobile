package com.dr.jjsembako.feature_order.presentation.create_order

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.ProductOrderList
import com.dr.jjsembako.ProductOrderStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuatPesananViewModel @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    private val productsDataStore: DataStore<ProductOrderList>
) : ViewModel() {

    private object PreferencesKeys {
        val ID_CUSTOMER = stringPreferencesKey("id_customer")
        val PAYMENT = booleanPreferencesKey("payment")
    }

    private val _idCustomer = MutableStateFlow("")
    val idCustomer: StateFlow<String> = _idCustomer

    private val _idCust = MutableLiveData<String>("")
    val idCust: LiveData<String> get() = _idCust

    private val _payment = MutableStateFlow(false)
    val payment: StateFlow<Boolean> = _payment

    private val _productsList = MutableStateFlow(ProductOrderList.getDefaultInstance())
    val productsList: StateFlow<ProductOrderList> = _productsList

    init {
        refresh()
    }

    fun reset(){
        viewModelScope.launch {
            setIdCustomer("")
            setPayment(false)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _idCustomer.value = getIdCustomer()
            _payment.value = getPayment()
//            _productsList.value = getProductsList()
            _idCust.value = idCustomer.value
        }
    }

    private suspend fun getIdCustomer(): String {
        return preferencesDataStore.data.first()[PreferencesKeys.ID_CUSTOMER] ?: ""
    }

    private suspend fun getPayment(): Boolean {
        return preferencesDataStore.data.first()[PreferencesKeys.PAYMENT] ?: false
    }

    private suspend fun getProductsList(): ProductOrderList {
        return productsDataStore.data.first()
    }

    suspend fun setIdCustomer(idCustomer: String) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.ID_CUSTOMER] = idCustomer
        }
        _idCustomer.value = idCustomer
    }

    suspend fun setPayment(payment: Boolean) {
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
}