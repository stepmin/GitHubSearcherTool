package com.example.www.domain.useCase

import com.example.www.domain.GitHubRepository

class UnStarRepositoryUseCase (
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(id: String) = repository.unstarRepo(id)
}