package com.example.www.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloExperimental
import com.apollographql.apollo.network.http.ApolloHttpNetworkTransport
import com.example.www.data.GitHubApi
import com.example.www.data.GitHubRepository
import com.example.www.data.GitHubStorage
import com.example.www.data.InMemoryGitHubStorage
import com.example.www.data.KtorGitHubApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }
    single<GitHubApi> { KtorGitHubApi(get()) }
    single<GitHubStorage> { InMemoryGitHubStorage() }
    single {
        GitHubRepository(get(), get()).apply {
            initialize()
        }
    }
}

@OptIn(ApolloExperimental::class, ExperimentalCoroutinesApi::class)
val apolloModule = module {
    single {
        val token = "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"
        val graphqlURL = "https://api.github.com/graphql"
        ApolloClient(
            networkTransport = ApolloHttpNetworkTransport(
                serverUrl = graphqlURL,
                headers = mapOf(
                    "Accept" to "application/json",
                    "Content-Type" to "application/json",
                    "Authorization" to "bearer $token"
                )
            )
        )
    }
}

fun initKoin() = initKoin(emptyList())

fun initKoin(extraModules: List<Module>) {
    startKoin {
        modules(
            dataModule,
            apolloModule,
            *extraModules.toTypedArray(),
        )
    }
}
