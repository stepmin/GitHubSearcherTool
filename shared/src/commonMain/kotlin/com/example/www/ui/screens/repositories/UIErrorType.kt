package com.example.www.ui.screens.repositories

sealed class UIErrorType {
    data class TOAST(val message: String) : UIErrorType()
}