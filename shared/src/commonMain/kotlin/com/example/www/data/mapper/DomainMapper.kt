package com.example.www.data.mapper

import com.example.www.data.model.dto.OwnerDto
import com.example.www.data.model.dto.RepositoriesDto
import com.example.www.data.model.dto.RepositoryDto
import com.example.www.domain.model.Owner
import com.example.www.domain.model.Repositories
import com.example.www.domain.model.RepositoryItem

fun RepositoriesDto.toDomain(): Repositories {
    return Repositories(
        total_count = totalCount,
        incomplete_results = incompleteResults,
        items = repositoryDtos.map { it.toDomain() }
    )
}

private fun RepositoryDto.toDomain(): RepositoryItem {
    return RepositoryItem(
        id = id,
        name = name,
        repoUrl = url,
        starsCount = stargazers_count,
        forksCount = forks_count,
        owner = owner.toDomain()
    )
}

private fun OwnerDto.toDomain(): Owner {
    return Owner(name = login, img = avatar_url)
}
