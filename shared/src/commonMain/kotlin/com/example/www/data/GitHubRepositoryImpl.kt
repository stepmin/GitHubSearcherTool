package com.example.www.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.www.data.datasource.ApolloDataSource
import com.example.www.data.datasource.GitHubRepoDataSource
import com.example.www.data.paging.RepositoriesPagingSource
import com.example.www.data.util.Result
import com.example.www.domain.GitHubRepository
import com.example.www.domain.model.Repository
import kotlinx.coroutines.flow.Flow

class GitHubRepositoryImpl(
    private val gitHubRepoDataSource: GitHubRepoDataSource,
    private val apolloDataSource: ApolloDataSource
) : GitHubRepository {

    override fun repositoriesDataStream(query: String): Flow<PagingData<Repository>> = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            RepositoriesPagingSource { page, pageSize ->
                gitHubRepoDataSource.fetchRepositories(query, page, pageSize)
            }
        }).flow

    override suspend fun starRepo(starrableId: String): Result<Boolean> {
        return apolloDataSource.starRepo(starrableId)
    }

    override suspend fun unstarRepo(starrableId: String): Result<Boolean> {
        return apolloDataSource.unstarRepo(starrableId)
    }
}