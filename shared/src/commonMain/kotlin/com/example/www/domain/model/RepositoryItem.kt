package com.example.www.domain.model

data class RepositoryItem(
    val id: Int?,
    val name: String?,
    val repoUrl: String?,
    val starsCount: Int?,
    val forksCount: Int?,
    val owner: Owner?
)