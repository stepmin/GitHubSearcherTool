package com.example.www.domain.model

data class Repositories(
    val totalCount: Int?,
    val incompleteResults: Boolean?,
    val items: List<Repository>?
)

