package com.example.www.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloExperimental
import com.apollographql.apollo.network.http.ApolloHttpNetworkTransport
import com.example.www.data.GitHubApi
import com.example.www.data.GitHubRepositoryImpl
import com.example.www.data.KtorGitHubApi
import com.example.www.domain.GitHubRepository
import com.example.www.domain.useCase.SearchRepositoriesUseCase
import com.example.www.screens.repositories.RepositoriesViewModel
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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

//TODO implement centralized token manipulation
const val gitHubToken = "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"

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

    single<GitHubRepository> { GitHubRepositoryImpl(get()) }
    single<GitHubApi> { KtorGitHubApi(get()) }
}

@OptIn(ApolloExperimental::class, ExperimentalCoroutinesApi::class)
val apolloModule = module {
    single {
        val graphqlEndpoint = "https://api.github.com/graphql"
        ApolloClient(
            networkTransport = ApolloHttpNetworkTransport(
                serverUrl = graphqlEndpoint,
                headers = mapOf(
                    "Accept" to "application/json",
                    "Content-Type" to "application/json",
                    "Authorization" to "bearer $gitHubToken"
                )
            )
        )
    }
}

private val searchUseCasesModule = module {
    singleOf(::SearchRepositoriesUseCase)
}

private val repositoriesViewModelModule = module {
    factory { RepositoriesViewModel(get()) }
}

fun initKoin() = startKoin {
        modules(
            dataModule,
            apolloModule,
            searchUseCasesModule,
            repositoriesViewModelModule
        )
    }
