package com.dr.jjsembako.feature_customer.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dr.jjsembako.core.data.remote.network.CustomerApiService
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import kotlinx.coroutines.CancellationException
import okio.IOException
import javax.inject.Inject

class CustomerPagingSource @Inject constructor(private val customerApiService: CustomerApiService) :
    PagingSource<Int, DataCustomer>() {

    private var searchQuery : String  = ""

    override fun getRefreshKey(state: PagingState<Int, DataCustomer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataCustomer> {
        try {
            // Mendapatkan nomor halaman yang diminta
            val page = params.key ?: 1

            // Mendapatkan data dari API
            if(searchQuery != "") {
                val response = customerApiService.fetchCustomers(
                        search = searchQuery,
                page = page,
                limit = params.loadSize
                )

                // Check jika response berhasil
                if (response.statusCode == 200) {
                    val data = response.data ?: emptyList()
                    return LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page + 1
                    )
                } else {
                    return LoadResult.Error(Throwable(response.message))
                }
            } else {
                val response = customerApiService.fetchCustomers(
                    page = page,
                    limit = params.loadSize
                )

                // Check jika response berhasil
                if (response.statusCode == 200) {
                    val data = response.data ?: emptyList()
                    return LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page + 1
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