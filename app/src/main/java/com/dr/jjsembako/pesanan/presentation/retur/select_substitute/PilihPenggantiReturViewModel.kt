package com.dr.jjsembako.pesanan.presentation.retur.select_substitute

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.SubstituteStore
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.pesanan.domain.model.SubstituteProduct
import com.dr.jjsembako.core.utils.DataMapper.mapListDataCategoryToListFilterOption
import com.dr.jjsembako.core.utils.DataMapper.mapSelectSubstituteItemToSubstituteStore
import com.dr.jjsembako.pesanan.data.SocketReturHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihPenggantiReturViewModel @Inject constructor(
    private val socketReturHandler: SocketReturHandler,
    private val substituteStore: DataStore<SubstituteStore>
) : ViewModel() {

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

    private val _dataProducts = MutableLiveData<List<SubstituteProduct?>>()
    val dataProducts: LiveData<List<SubstituteProduct?>> get() = _dataProducts

    private val _selectedData = MutableLiveData<SubstituteProduct?>()
    val selectedData: LiveData<SubstituteProduct?> get() = _selectedData

    private val _substituteData = MutableLiveData<SubstituteStore?>()
    private val substituteData: LiveData<SubstituteStore?> get() = _substituteData

    init {
        viewModelScope.launch {
            _substituteData.value = getSubstituteStore()
        }
        initSocket()
    }

    fun saveData() {
        if (substituteData.value == null) return
        else {
            viewModelScope.launch {
                setSubstituteStore(substituteData.value)
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            setSubstituteStore()
            _substituteData.value = getSubstituteStore()
        }
        if (selectedData.value == null) return
        else disableOrder(selectedData.value!!)
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

    private fun recoveryData() {
        if (substituteData.value == null) return
        else {
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
                    _selectedData.value = updatedExistingProduct
                    _dataProducts.value = currentList
                }
            }
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
                            selledPrice = updatedProduct.standardPrice,
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
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (price.isNotEmpty()) {
                    val orderPrice = price.toLong()
                    if (orderPrice != existingProduct.selledPrice) {
                        val updatedExistingProduct = existingProduct.copy(
                            selledPrice = orderPrice
                        )
                        currentList[productIndex] = updatedExistingProduct
                        currentList.remove(existingProduct)
                        setSubstituteStore(
                            mapSelectSubstituteItemToSubstituteStore(
                                updatedExistingProduct
                            )
                        )
                        _substituteData.value = getSubstituteStore()
                        _selectedData.value = updatedExistingProduct
                        _dataProducts.value = currentList
                    }
                } else {
                    disableOrder(existingProduct)
                }
            }
        }
    }

    fun enableOrder(product: SubstituteProduct) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (!existingProduct.isChosen) {
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = true,
                        selledPrice = existingProduct.standardPrice,
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    setSubstituteStore(
                        mapSelectSubstituteItemToSubstituteStore(
                            updatedExistingProduct
                        )
                    )
                    _substituteData.value = getSubstituteStore()
                    _selectedData.value = updatedExistingProduct
                }

                _dataProducts.value = currentList
            }
        }
    }

    fun disableOrder(product: SubstituteProduct) {
        viewModelScope.launch {
            val currentList = _dataProducts.value.orEmpty().toMutableList()
            val productIndex = currentList.indexOfFirst { it?.id == product.id }

            if (productIndex != -1) {
                val existingProduct = currentList[productIndex]!!

                if (existingProduct.isChosen) {
                    val updatedExistingProduct = existingProduct.copy(
                        isChosen = false,
                        selledPrice = 0
                    )
                    currentList[productIndex] = updatedExistingProduct
                    currentList.remove(existingProduct)
                    setSubstituteStore()
                    _substituteData.value = getSubstituteStore()
                    _selectedData.value = null
                }

                _dataProducts.value = currentList
            }
        }
    }

    private fun updateCategories(newProducts: List<SubstituteProduct?>) {
        viewModelScope.launch {
            val newCategories = newProducts.mapNotNull { it?.category }.distinct()
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

    private fun initSocket() {
        // Connect to Socket
        socketReturHandler.connect()

        // Set up callbacks for Socket events
        socketReturHandler.onProductsReceived = { products ->
            viewModelScope.launch {
                _dataProducts.value = products
                _loadingState.value = false
                updateCategories(products)
                recoveryData()
            }
        }

        socketReturHandler.onNewProductReceived = { newProduct ->
            viewModelScope.launch {
                val currentList = _dataProducts.value.orEmpty().toMutableList()
                currentList.add(0, newProduct)
                _dataProducts.value = currentList
                updateCategories(listOf(newProduct))
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