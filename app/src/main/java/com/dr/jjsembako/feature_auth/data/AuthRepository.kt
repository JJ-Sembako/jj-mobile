package com.dr.jjsembako.feature_auth.data

import com.dr.jjsembako.core.common.ApiResponse
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataHandleLogin
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authDataSource: AuthDataSource) :
    IAuthRepository {

    override suspend fun handleLogin(
        username: String,
        password: String
    ): Flow<Resource<DataHandleLogin?>> = flow {
        emit(Resource.Loading())
        when (val response = authDataSource.handleLogin(username, password).first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                if (response.data.statusCode == 200) {
                    val user = response.data.data
                    emit(Resource.Success(user, response.data.message, response.data.statusCode))
                } else emit(Resource.Error(response.data.message))

            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}