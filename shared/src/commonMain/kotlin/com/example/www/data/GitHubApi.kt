package com.example.www.data

import com.example.www.data.mapper.toDomain
import com.example.www.data.model.dto.RepositoriesDto
import com.example.www.data.model.dto.StarredRepositoryDto
import com.example.www.data.util.Result
import com.example.www.di.gitHubToken
import com.example.www.domain.model.Repository
import com.example.www.domain.model.StarredRepo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.isSuccess

interface GitHubApi {
    suspend fun fetchRepositories(query: String, page: Int, pageSize: Int): Result<Pair<List<Repository>, Headers>>
}

class KtorGitHubApi(private val client: HttpClient) : GitHubApi {
    companion object {
        private const val SEARCH_API_URL = "https://api.github.com/search/repositories"
        private const val STARRED_REPOSITORIES_API_URL = "https://api.github.com/user/starred"
    }

    override suspend fun fetchRepositories(query: String, page: Int, pageSize: Int): Result<Pair<List<Repository>, Headers>> {
        return try {
            val starredRepositories = remoteStarredRepositories()

            val httpResponse = client.get(SEARCH_API_URL) {
                header("Authorization", "bearer $gitHubToken")
                parameter("q", query)
                parameter("page", page)
                parameter("per_page", pageSize)
            }

            if (httpResponse.status.isSuccess()) {
                val value = httpResponse.body<RepositoriesDto>().toDomain()
                val combinedData = value.items?.map { repo ->
                    repo.copy(isStarred = starredRepositories.any { it.nodeId == repo.nodeId })
                }
                val pair = Pair(combinedData ?: emptyList(), httpResponse.headers)
                return Result.Success(pair)
            } else {
                throw Exception("Request failed with status code: ${httpResponse.status.value}")
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    //TODO-error handling
    private suspend fun remoteStarredRepositories(): List<StarredRepo> {
        val allStarredRepos = mutableListOf<StarredRepo>()
        var page = 1
        var hasNextPage = true

        while (hasNextPage) {
            val response: HttpResponse = client.get(STARRED_REPOSITORIES_API_URL) {
                parameter("page", page)
                // Add authentication headers if needed
                header("Authorization", "bearer $gitHubToken")
            }

            if (response.status.isSuccess()) {
                val pageRepositories: List<StarredRepo> = response.body<List<StarredRepositoryDto>>()
                    .filter { it.nodeId != null }
                    .toDomain()
                allStarredRepos.addAll(pageRepositories)

                // Check for next page using Link header or other pagination information
                hasNextPage = response.headers["Link"]?.contains("rel=\"next\"") ?: false
                page++
            } else {
                // Handle API error
                break
            }
        }

        return allStarredRepos
    }

}
