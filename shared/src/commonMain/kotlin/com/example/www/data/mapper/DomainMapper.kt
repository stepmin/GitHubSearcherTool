package com.example.www.data.mapper

import com.example.www.data.model.dto.RepositoriesDto
import com.example.www.data.model.dto.RepositoryDto
import com.example.www.domain.model.Repositories
import com.example.www.domain.model.Repository

fun RepositoriesDto.toDomain(): Repositories {
    return Repositories(
        total_count = totalCount,
        incomplete_results = incompleteResults,
        items = repositoryDtos?.filter { it.node_id != null && it.owner?.login != null }?.map { it.toDomain() }
    )
}

fun RepositoryDto.toDomain(): Repository {
    return Repository(
        id = node_id ?: "",
        name = name,
        repoUrl = url,
        starsCount = stargazers_count,
        forksCount = forks_count,
        ownerLogin = owner?.login ?: "",
        ownerImage = owner?.avatar_url
    )
}