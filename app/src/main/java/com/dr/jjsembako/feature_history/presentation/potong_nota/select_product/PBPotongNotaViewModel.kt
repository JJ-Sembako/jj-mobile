package com.dr.jjsembako.feature_history.presentation.potong_nota.select_product

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import com.dr.jjsembako.CanceledStore
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PBPotongNotaViewModel @Inject constructor(
    private val canceledStore: DataStore<CanceledStore>,
    private val fetchOrderUseCase: FetchOrderUseCase
) : ViewModel() {
}