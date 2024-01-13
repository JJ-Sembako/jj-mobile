package com.dr.jjsembako.feature_order.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dr.jjsembako.core.data.model.DataCustomerOrder
import com.dr.jjsembako.core.data.remote.network.CustomerApiService
import com.dr.jjsembako.core.utils.DataMapper.mapListCustDataToListCustDataOrder
import kotlinx.coroutines.CancellationException
import okio.IOException
import javax.inject.Inject

class SelectCustPagingSource @Inject constructor(private val customerApiService: CustomerApiService) :
    PagingSource<Int, DataCustomerOrder>() {

    private var searchQuery: String = ""
    private var currentPage = 0
    private var totalData = 0

    override fun getRefreshKey(state: PagingState<Int, DataCustomerOrder>): Int? {
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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataCustomerOrder> {
        try {
            // Mendapatkan nomor halaman yang diminta
            currentPage = params.key ?: 1

            // Mendapatkan data dari API
            if (searchQuery != "") {
                val response = customerApiService.fetchCustomers(
                    search = searchQuery,
                    page = currentPage,
                    limit = params.loadSize
                )

                // Check jika response berhasil
                if (response.statusCode == 200) {
                    totalData = response.count ?: 0
                    val data = mapListCustDataToListCustDataOrder(response.data)
                    return LoadResult.Page(
                        data = data,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (data.isEmpty() || currentPage >= totalData / params.loadSize + 1) null else currentPage + 1
                    )
                } else {
                    return LoadResult.Error(Throwable(response.message))
                }
            } else {
                val response = customerApiService.fetchCustomers(
                    page = currentPage,
                    limit = params.loadSize
                )

                // Check jika response berhasil
                if (response.statusCode == 200) {
                    totalData = response.count ?: 0
                    val data = mapListCustDataToListCustDataOrder(response.data)
                    return LoadResult.Page(
                        data = data,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (data.isEmpty() || currentPage >= totalData / params.loadSize + 1) null else currentPage + 1
                    )
                } else {
                    return LoadResult.Error(Throwable(response.message))
                }
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

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery = searchQuery
    }
}