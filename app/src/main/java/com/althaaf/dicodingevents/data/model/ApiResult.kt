package com.althaaf.dicodingevents.data.model

sealed class ApiResult<out R> {
    data class Success<T>(val data: T): ApiResult<T>()
    data class Error(val errorMessage: String): ApiResult<Nothing>()
    data object Loading: ApiResult<Nothing>()
    data object Empty: ApiResult<Nothing>()
}