package com.example.www.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.www.data.repository.paging.RepositoriesPagingSource
import com.example.www.domain.GitHubRepository
import com.example.www.domain.model.Repository
import kotlinx.coroutines.flow.Flow

class GitHubRepositoryImpl(
    private val gitHubApi: GitHubApi,
) : GitHubRepository {

    override fun repositoriesDataStream(query: String): Flow<PagingData<Repository>> = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            RepositoriesPagingSource(
                fetch = { page, pageSize ->
                    gitHubApi.searchForRepository(query, page, pageSize)
                }
            )
        }).flow
}