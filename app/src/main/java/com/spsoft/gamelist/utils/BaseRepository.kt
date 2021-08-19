package com.spsoft.gamelist.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.HttpException

abstract class BaseRepository {
    suspend fun <T> saveApiCall( apiCall: suspend () -> T): NetworkResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> NetworkResponse.Failure("HttpException", throwable)
                    else -> NetworkResponse.Failure("Error", throwable)
                }
            }
        }

    }
}