package com.example.www.data.util

sealed interface Result<out R> {
    class Success<out R>(val value: R) : Result<R>
    data object Loading : Result<Nothing>
    class Error(val throwable: Throwable) : Result<Nothing>
}