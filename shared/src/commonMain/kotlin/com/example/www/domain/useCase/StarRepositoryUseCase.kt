package com.example.www.domain.useCase

import com.example.www.domain.GitHubRepository

class StarRepositoryUseCase (
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(id: String, shouldStar: Boolean) = repository.starUnstarRepo(id, shouldStar)
}