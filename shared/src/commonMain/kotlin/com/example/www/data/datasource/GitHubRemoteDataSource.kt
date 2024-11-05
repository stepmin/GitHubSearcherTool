package com.example.www.data.datasource

import com.example.www.data.GitHubApi
import com.example.www.data.datasource.db.AppDatabase
import com.example.www.data.datasource.db.entities.StarredRepositoryEntity
import com.example.www.data.util.Result
import com.example.www.domain.model.Repository
import io.ktor.http.Headers

class GitHubRemoteDataSource(
    private val gitHubApi: GitHubApi,
    private val appDatabase: AppDatabase
) : GitHubRepoDataSource {
    override suspend fun fetchRepositories(query: String, page: Int, pageSize: Int): Result<Pair<List<Repository>, Headers>> {
        val fetchRepositories = gitHubApi.fetchRepositories(query, page, pageSize)
        if (fetchRepositories is Result.Success) {
            val starredRepositories = fetchRepositories.value.first.filter { it.isStarred }.toDbEntity()
            appDatabase.starredRepositoryDao().insertAll(starredRepositories)
        }
        return fetchRepositories
    }
}

private fun List<Repository>.toDbEntity(): List<StarredRepositoryEntity> {
    return this.map {
        StarredRepositoryEntity(nodeId = it.toString())
    }
}