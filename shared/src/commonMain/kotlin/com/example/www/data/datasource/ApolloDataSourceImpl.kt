package com.example.www.data.datasource

import AddStarMutation
import RemoveStarMutation
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloExperimental
import com.example.www.data.datasource.db.AppDatabase
import com.example.www.data.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single

@OptIn(ApolloExperimental::class, ExperimentalCoroutinesApi::class)
class ApolloDataSourceImpl(
    private val apolloClient: ApolloClient,
    private val appDatabase: AppDatabase
) : ApolloDataSource {
    override suspend fun starRepo(starrableId: String): Result<Boolean> {
        return try {
            val hasErrors = apolloClient.mutate(AddStarMutation(starrableId)).execute().single().hasErrors()
            if (!hasErrors) {
                appDatabase.starredRepositoryDao().starRepository(starrableId)
                Result.Success(true)
            } else {
                Result.Success(false)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun unstarRepo(starrableId: String): Result<Boolean> {
        return try {
            val hasErrors = apolloClient.mutate(RemoveStarMutation(starrableId)).execute().single().hasErrors()
            return if (!hasErrors) {
                appDatabase.starredRepositoryDao().unstarRepository(starrableId)
                Result.Success(true)
            } else {
                Result.Success(false)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}