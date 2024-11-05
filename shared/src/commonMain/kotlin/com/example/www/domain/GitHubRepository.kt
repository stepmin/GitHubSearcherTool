package com.example.www.domain

import androidx.paging.PagingData
import com.example.www.data.util.Result
import com.example.www.domain.model.Repository
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    fun repositoriesDataStream(query: String): Flow<PagingData<Repository>>
    suspend fun starRepo(starrableId: String): Result<Boolean>
    suspend fun unstarRepo(starrableId: String): Result<Boolean>
}