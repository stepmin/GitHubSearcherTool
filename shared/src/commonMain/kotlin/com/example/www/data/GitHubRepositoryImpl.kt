package com.example.www.data

import AddStarMutation
import RemoveStarMutation
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloException
import com.apollographql.apollo.ApolloHttpException
import com.apollographql.apollo.ApolloNetworkException
import com.apollographql.apollo.api.ApolloExperimental
import com.example.www.data.datasource.db.AppDatabase
import com.example.www.data.datasource.db.entities.StarredRepositoryEntity
import com.example.www.data.paging.RepositoriesPagingSource
import com.example.www.data.util.Result
import com.example.www.domain.GitHubRepository
import com.example.www.domain.model.Repository
import io.ktor.http.Headers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class GitHubRepositoryImpl(
    private val gitHubApi: GitHubApi,
    private val appDatabase: AppDatabase,
    private val apolloClient: ApolloClient
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

    //TODO-error handling
    @OptIn(ApolloExperimental::class, ExperimentalCoroutinesApi::class)
    override suspend fun starUnstarRepo(starrableId: String, shouldStar: Boolean) {
        try {
            val mutate = when (shouldStar) {
                true -> apolloClient.mutate(AddStarMutation(starrableId)).execute()
                false -> apolloClient.mutate(RemoveStarMutation(starrableId)).execute()
            }
            mutate.collect {
                if (!it.hasErrors()) {
                    if (shouldStar) {
                        appDatabase.starredRepositoryDao().starRepository(starrableId)
                    } else {
                        appDatabase.starredRepositoryDao().unstarRepository(starrableId)
                    }
                } else {
                    println(it.errors)
                }
            }
        } catch (e: ApolloNetworkException) {
            println(e.message)
        } catch (e: ApolloHttpException) {
            println(e.message)
        } catch (e: ApolloException) {
            println(e.message)
        }
    }
}