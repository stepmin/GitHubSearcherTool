package com.example.www.domain.useCase

import com.example.www.domain.GitHubRepository

class SearchRepositoriesUseCase (
    private val repository: GitHubRepository
) {
    operator fun invoke(input: String) = repository.repositoriesDataStream(input)

}