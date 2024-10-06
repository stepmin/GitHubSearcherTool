package com.example.www.domain.useCase

import AddStarMutation
import RemoveStarMutation
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloException
import com.apollographql.apollo.api.ApolloExperimental
import kotlinx.coroutines.ExperimentalCoroutinesApi

class StarRepositoryUseCase (
    private val apolloClient: ApolloClient
) {
    @OptIn(ApolloExperimental::class, ExperimentalCoroutinesApi::class)
    suspend fun execute(starrableId: String, starUnstar: Boolean) {
        try {
            val mutate = if (starUnstar) {
                val addStarMutation = AddStarMutation(starrableId)
                apolloClient.mutate(addStarMutation).execute()
            } else {
                val removeStarMutation = RemoveStarMutation(starrableId)
                apolloClient.mutate(removeStarMutation).execute()
            }
            mutate.collect {
                if (it.hasErrors()) {
                    println(it.errors)
                } else {
                    println(it.data)
                }
            }
        } catch (e: ApolloException) {
            println(e.message)
        }
    }
}