package com.example.www.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.www.data.datasource.db.AppDatabase
import com.example.www.data.datasource.db.entities.StarredRepositoryEntity
import com.example.www.data.repository.paging.RepositoriesPagingSource
import com.example.www.data.util.Result
import com.example.www.domain.GitHubRepository
import com.example.www.domain.model.Repository
import io.ktor.http.Headers
import kotlinx.coroutines.flow.Flow

class GitHubRepositoryImpl(
    private val gitHubApi: GitHubApi,
    private val appDatabase: AppDatabase
) : GitHubRepository {

    override fun repositoriesDataStream(query: String): Flow<PagingData<Repository>> = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            RepositoriesPagingSource { page, pageSize ->
                val fetchResponse: Result<Pair<List<Repository>, Headers>> = gitHubApi.fetchRepositories(query, page, pageSize)
                saveStarredToDb(fetchResponse)
                fetchResponse
            }
        }).flow

    private suspend fun saveStarredToDb(fetchResponse: Result<Pair<List<Repository>, Headers>>) {
        if (fetchResponse is Result.Success) {
            val starredRepositories = fetchResponse.value.first.filter { it.isStarred }.toDbEntity()
            appDatabase.starredRepositoryDao().insertAll(starredRepositories)
        }
    }

    private fun List<Repository>.toDbEntity(): List<StarredRepositoryEntity> {
        return this.map {
            StarredRepositoryEntity(nodeId = it.toString())
        }
    }
}