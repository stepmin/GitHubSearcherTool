package com.example.www.data

import com.example.www.domain.model.Repositories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface GitHubStorage {
    suspend fun saveObjects(newObjects: Repositories)

    fun getRepositories(): Flow<Repositories>
}

class InMemoryGitHubStorage : GitHubStorage {
    private val storedObjects = MutableStateFlow(Repositories(0, false, null))

    override suspend fun saveObjects(newObjects: Repositories) {
        storedObjects.value = newObjects
    }

    override fun getRepositories(): Flow<Repositories> = storedObjects
}
