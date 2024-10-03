package com.example.www.data

import com.example.www.domain.model.Repositories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GitHubRepository(
    private val gitHubApi: GitHubApi,
    private val gitHubStorage: GitHubStorage,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            init()
        }
    }

    private suspend fun init() {
        val repositories = gitHubApi.getRepositories()
        if (repositories.isSuccess) {
            val newObjects = repositories.getOrNull()
            if (newObjects != null) {
                gitHubStorage.saveObjects(newObjects)
            }
        }
    }

    fun getRepositories(): Flow<Repositories> = gitHubStorage.getRepositories()
}