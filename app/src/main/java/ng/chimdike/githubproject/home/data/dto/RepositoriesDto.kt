package ng.chimdike.githubproject.home.data.dto

data class RepositoryDto(
    val incompleteResults: Boolean,
    val items: List<RepositoryItemDto>,
    val totalCount: Int
)

data class RepositoryItemDto(
    val description: String,
    val fullName: String,
    val language: String,
    val name: String,
    val owner: OwnerDto,
    val stargazersCount: Int,
    val topics: List<String>,
    val visibility: String
)

data class OwnerDto(
    val avatarUrl: String,
    val login: String
)
