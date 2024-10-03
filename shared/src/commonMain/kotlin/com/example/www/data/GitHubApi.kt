package com.example.www.data

import com.example.www.data.mapper.toDomain
import com.example.www.data.model.dto.RepositoriesDto
import com.example.www.domain.model.Repositories
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlin.coroutines.cancellation.CancellationException

interface GitHubApi {
    suspend fun getRepositories(): Result<Repositories>
}

class KtorGitHubApi(private val client: HttpClient) : GitHubApi {
    companion object {
        private const val API_URL = "https://api.github.com/search/repositories?q=KMP"
    }

    override suspend fun getRepositories(): Result<Repositories> {
        return try {
            val httpResponse = client.get(API_URL)
            //TODO-QUERY PARAMETERS
/*            val httpResponse = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.github.com"
                    path(API_URL)
                }
            }*/

            if (httpResponse.status.isSuccess()) {
                val value: Repositories = httpResponse.body<RepositoriesDto>().toDomain()
                return Result.success(value)
            } else {
                throw Exception("Request failed with status code: ${httpResponse.status.value}")
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            Result.failure(e)
        }
    }
}