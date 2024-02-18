package com.dr.jjsembako.feature_history.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dr.jjsembako.core.data.remote.network.OrderApiService
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import kotlinx.coroutines.CancellationException
import okio.IOException
import javax.inject.Inject

class HistoryPagingSource @Inject constructor(private val orderApiService: OrderApiService) :
    PagingSource<Int, OrderDataItem>() {

    private var search: String? = null
    private var minDate: String? = null
    private var maxDate: String? = null
    private var me: Int? = null
    private var currentPage = 0
    private var totalData = 0

    override fun getRefreshKey(state: PagingState<Int, OrderDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            if (closestPage != null && closestPage.prevKey != null) {
                closestPage.prevKey
            } else if (closestPage != null && closestPage.nextKey != null && currentPage < totalData) {
                closestPage.nextKey
            } else {
                null
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderDataItem> {
        try {
            // Mendapatkan nomor halaman yang diminta
            currentPage = params.key ?: 1

            // Mendapatkan data dari API
            val response = orderApiService.fetchOrders(
                search = search,
                minDate = minDate,
                maxDate = maxDate,
                me = me,
                page = currentPage,
                limit = params.loadSize
            )

            // Check jika response berhasil
            if (response.statusCode == 200) {
                totalData = response.count ?: 0
                val data = response.data ?: emptyList()
                return LoadResult.Page(
                    data = data,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (data.isEmpty() || currentPage >= totalData / params.loadSize + 1) null else currentPage + 1
                )
            } else {
                return LoadResult.Error(Throwable(response.message))
            }
        } catch (e: CancellationException) {
            // Do nothing, the flow is cancelled
            return LoadResult.Error(Throwable("Terjadi pembatalan request"))
        } catch (e: IOException) {
            return LoadResult.Error(Throwable("Masalah jaringan koneksi internet"))
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    fun setParams(
        search: String? = null,
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null
    ) {
        this.search = search
        this.minDate = minDate
        this.maxDate = maxDate
        this.me = me
    }
}