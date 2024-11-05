package com.example.www.data.datasource

import com.example.www.data.util.Result

interface ApolloDataSource {
    suspend fun starRepo(starrableId: String): Result<Boolean>
    suspend fun unstarRepo(starrableId: String): Result<Boolean>
}