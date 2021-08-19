package com.spsoft.gamelist.utils

sealed class NetworkResponse<out T> {
    data class Success<out R>(val data: R) : NetworkResponse<R>()
    data class Failure(
        val message: String,
        val throwable: Throwable
    ) : NetworkResponse<Nothing>()

}