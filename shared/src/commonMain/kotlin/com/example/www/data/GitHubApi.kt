package com.example.www.data

import com.example.www.data.mapper.toDomain
import com.example.www.data.model.dto.RepositoriesDto
import com.example.www.data.util.Result
import com.example.www.di.gitHubToken
import com.example.www.domain.model.Repository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.Headers
import io.ktor.http.isSuccess
import kotlin.coroutines.cancellation.CancellationException

interface GitHubApi {
    suspend fun searchForRepository(query: String, page: Int, pageSize: Int): Result<Pair<List<Repository>, Headers>>
}

class KtorGitHubApi(private val client: HttpClient) : GitHubApi {
    companion object {
        private const val SEARCH_API_URL = "https://api.github.com/search/repositories"
    }

    override suspend fun searchForRepository(query: String, page: Int, pageSize: Int): Result<Pair<List<Repository>, Headers>> {
        return try {
            val httpResponse = client.get(SEARCH_API_URL) {
                header("Authorization", "bearer $gitHubToken")
                parameter("q", query)
                parameter("page", page)
                parameter("per_page", pageSize)
            }

            if (httpResponse.status.isSuccess()) {
                val value = httpResponse.body<RepositoriesDto>().toDomain()
                val pair = Pair(value.items ?: emptyList(), httpResponse.headers)
                return Result.Success(pair)
            } else {
                throw Exception("Request failed with status code: ${httpResponse.status.value}")
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            Result.Error(e)
        }
    }
}
