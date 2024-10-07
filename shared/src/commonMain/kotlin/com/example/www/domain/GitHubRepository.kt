package com.example.www.domain

import androidx.paging.PagingData
import com.example.www.domain.model.Repository
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    fun repositoriesDataStream(query: String): Flow<PagingData<Repository>>
    suspend fun starUnstarRepo(starrableId: String, shouldStar: Boolean)
}