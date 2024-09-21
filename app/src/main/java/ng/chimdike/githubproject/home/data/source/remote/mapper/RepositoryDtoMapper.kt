package com.chimdike.home.data.source.remote.mapper

import ng.chimdike.githubproject.home.data.dto.OwnerDto
import ng.chimdike.githubproject.home.data.dto.RepositoryDto
import ng.chimdike.githubproject.home.data.dto.RepositoryItemDto
import com.chimdike.home.data.source.api.model.repositories.RepositoryResponse
import javax.inject.Inject



internal  interface  RepositoryDtoMapper{
    fun map(response: RepositoryResponse): RepositoryDto
}

class RepositoryDtoMapperImpl @Inject constructor(): RepositoryDtoMapper {
    override fun map(response: RepositoryResponse): RepositoryDto {
         val repoList = response.items.map { repo ->

             val owner = OwnerDto(
                 login = repo.owner.login,
                 avatarUrl = repo.owner.avatarUrl,
             )

            RepositoryItemDto(
             description=repo.description ?: "",
             fullName=repo.fullName,
             language=repo.language ?: "",
             name=repo.name,
             owner= owner,
             stargazersCount = repo.stargazersCount ?: 0,
             topics = repo.topics ?: emptyList(),
             visibility=repo.visibility,
            )
        }
        return  RepositoryDto(
            totalCount = response.totalCount,
            incompleteResults = response.incompleteResults,
            items = repoList,
        )
    }

}