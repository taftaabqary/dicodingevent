package com.althaaf.dicodingevents.data.model

data class StateApi<T>(
    val data: T? = null,
    var error: String? = null,
    var isLoading: Boolean = false
)

