package com.dr.jjsembako.feature_auth.data

import android.accounts.Account
import com.dr.jjsembako.core.common.ApiResponse
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.utils.AppExecutors
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val appExecutors: AppExecutors
) : IAuthRepository {
    override suspend fun handleLogin(username: String, password: String): Flow<Resource<Account>> {
//        return flow {
//            emit(Resource.Loading())
//            try {
//                val response = authDataSource.handleLogin(username, password)
//                if (response.) {
//                    val account = Account(
//                        response.body?.data?.username ?: "",
//                        response.body?.data?.role ?: ""
//                    )
//                    emit(Resource.Success(account))
//                } else {
//                    emit(Resource.Error(response.message))
//                }
//            } catch (e: Exception) {
//                emit(Resource.Error(e.message))
//            }
//        }.flowOn(Dispatchers.IO)
    }

}