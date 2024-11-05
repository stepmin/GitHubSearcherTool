package com.example.www.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloExperimental
import com.apollographql.apollo.network.http.ApolloHttpNetworkTransport
import com.example.www.data.GitHubApi
import com.example.www.data.GitHubRepositoryImpl
import com.example.www.data.KtorGitHubApi
import com.example.www.data.datasource.ApolloDataSource
import com.example.www.data.datasource.ApolloDataSourceImpl
import com.example.www.data.datasource.GitHubRepoDataSource
import com.example.www.data.datasource.GitHubRemoteDataSource
import com.example.www.domain.GitHubRepository
import com.example.www.domain.useCase.SearchRepositoriesUseCase
import com.example.www.domain.useCase.StarRepositoryUseCase
import com.example.www.domain.useCase.UnStarRepositoryUseCase
import com.example.www.ui.screens.repositories.RepositoriesViewModel
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

@OptIn(ApolloExperimental::class)
val dataModule = module {
    single {
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

    single<GitHubRepoDataSource> { GitHubRemoteDataSource(get(), get()) }
    single<ApolloDataSource> { ApolloDataSourceImpl(get(), get()) }
    single<GitHubRepository> { GitHubRepositoryImpl(get(), get()) }
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
    singleOf(::UnStarRepositoryUseCase)
}

private val repositoriesViewModelModule = module {
    factory { RepositoriesViewModel(get(), get(), get()) }
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
