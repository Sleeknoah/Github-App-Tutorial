package com.chimdike.home.data.source.remote.mapper

import ng.chimdike.githubproject.home.data.dto.OwnerDto
import ng.chimdike.githubproject.home.data.dto.RepositoryItemDto
import com.chimdike.home.data.source.api.model.repositories.Item
import javax.inject.Inject



internal interface UserRepositoryDtoMapper{
    fun map(response: List<Item>): List<RepositoryItemDto>
}

class UserRepositoryDtoMapperImpl @Inject constructor(): UserRepositoryDtoMapper {
    override fun map(response: List<Item>): List<RepositoryItemDto> {

        val repoList = response.map { repo ->

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
        return  repoList
    }


}