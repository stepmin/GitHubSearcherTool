package com.example.www.domain.model

data class Repository(
    val nodeId: String,
    val name: String?,
    val repoUrl: String?,
    val starsCount: Int?,
    val forksCount: Int?,
    val ownerLogin: String,
    val ownerImage: String?,
    val isStarred: Boolean = false
)