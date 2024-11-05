package com.example.www.data.datasource

import com.example.www.data.util.Result
import com.example.www.domain.model.Repository
import io.ktor.http.Headers

interface GitHubRepoDataSource {
    suspend fun fetchRepositories(query: String, page: Int, pageSize: Int): Result<Pair<List<Repository>, Headers>>
}