package com.example.www.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloExperimental
import com.apollographql.apollo.network.http.ApolloHttpNetworkTransport
import com.example.www.data.GitHubApi
import com.example.www.data.GitHubRepositoryImpl
import com.example.www.data.KtorGitHubApi
import com.example.www.domain.GitHubRepository
import com.example.www.domain.useCase.SearchRepositoriesUseCase
import com.example.www.domain.useCase.StarRepositoryUseCase
import com.example.www.screens.repositories.RepositoriesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
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
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }

    single<GitHubRepository> { GitHubRepositoryImpl(get(), get(), get()) }
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

private val useCasesModule = module {
    singleOf(::SearchRepositoriesUseCase)
    singleOf(::StarRepositoryUseCase)
}

private val repositoriesViewModelModule = module {
    factory { RepositoriesViewModel(get(), get()) }
}

expect fun platformModule(): Module

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(
        platformModule(),
        dataModule,
        apolloModule,
        useCasesModule,
        repositoriesViewModelModule
    )
}
