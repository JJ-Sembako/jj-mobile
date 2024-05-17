package com.dr.jjsembako.pesanan.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dr.jjsembako.core.data.remote.network.CustomerApiService
import com.dr.jjsembako.pelanggan.domain.model.Customer
import kotlinx.coroutines.CancellationException
import okio.IOException
import javax.inject.Inject

class SelectCustPagingSource @Inject constructor(private val customerApiService: CustomerApiService) :
    PagingSource<Int, Customer>() {

    private var searchQuery: String = ""
    private var currentPage = 0
    private var totalData = 0

    override fun getRefreshKey(state: PagingState<Int, Customer>): Int? {
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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Customer> {
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
                    val data = response.data ?: emptyList()
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
                    val data = response.data ?: emptyList()
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