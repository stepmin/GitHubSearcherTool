package com.example.www.data.mapper

import com.example.www.data.model.dto.RepositoriesDto
import com.example.www.data.model.dto.RepositoryDto
import com.example.www.data.model.dto.StarredRepositoryDto
import com.example.www.domain.model.Repositories
import com.example.www.domain.model.Repository
import com.example.www.domain.model.StarredRepo

fun RepositoriesDto.toDomain(): Repositories {
    return Repositories(
        totalCount = totalCount,
        incompleteResults = incompleteResults,
        items = repositoryDtos?.filter { it.node_id != null && it.owner?.login != null }?.map { it.toDomain() }
    )
}

fun RepositoryDto.toDomain(): Repository {
    return Repository(
        nodeId = node_id ?: "",
        name = name,
        repoUrl = url,
        starsCount = stargazers_count,
        forksCount = forks_count,
        ownerLogin = owner?.login ?: "",
        ownerImage = owner?.avatar_url
    )
}

fun List<StarredRepositoryDto>.toDomain(): List<StarredRepo> {
    return this.map { it.toDomain() }
}

fun StarredRepositoryDto.toDomain(): StarredRepo {
    return StarredRepo(
        name = name ?: "",
        nodeId = nodeId ?: "",
        repoUrl = url,
    )
}