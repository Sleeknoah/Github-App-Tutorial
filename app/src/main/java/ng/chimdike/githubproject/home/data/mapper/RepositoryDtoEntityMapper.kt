package com.chimdike.home.data.mapper


import ng.chimdike.githubproject.home.data.dto.RepositoryDto
import com.chimdike.home.domain.entity.OwnerResult
import com.chimdike.home.domain.entity.RepositoriesResult
import com.chimdike.home.domain.entity.RepositoryEntityResult
import com.chimdike.home.domain.entity.RepositoryItemResult
import javax.inject.Inject

internal interface RepositoryDtoEntityMapper{
    fun map(dto: RepositoryDto): RepositoryEntityResult
    fun map(exception: Exception): RepositoryEntityResult
}

class RepositoryDtoEntityMapperImpl @Inject constructor(): RepositoryDtoEntityMapper {
    override fun map(dto: RepositoryDto): RepositoryEntityResult {
        val repoList = dto.items.map { repo ->

            val owner = OwnerResult(
                login = repo.owner.login,
                avatarUrl = repo.owner.avatarUrl,
            )

            RepositoryItemResult(
                description=repo.description,
                fullName=repo.fullName,
                language=repo.language,
                name=repo.name,
                owner= owner,
                stargazersCount = repo.stargazersCount,
                topics = repo.topics,
                visibility=repo.visibility,
            )
        }
        val result = RepositoriesResult(
            totalCount = dto.totalCount,
            incompleteResults = dto.incompleteResults,
            items = repoList,
        )
        return  RepositoryEntityResult.Success(result)
    }

    override fun map(exception: Exception): RepositoryEntityResult {
        return RepositoryEntityResult.Failure(
            message = exception.message ?: "Oops an error just occurred. Please try again"
        )
    }


}