package feature.person.presentation.detail.model

data class PersonDetailUiModel(
    val id: Int,
    val imagePath: String?,
    val name: String,
    val knownForDepartment: String,
    val birthday: String,
    val deathday: String,
    val placeOfBirth: String,
    val biography: String,
    val castMovies: List<ProductionUiModel>,
    val crewMovies: List<ProductionUiModel>,
    val castTvShows: List<ProductionUiModel>,
    val crewTvShows: List<ProductionUiModel>,
) {
    companion object {
        fun initial(id: Int): PersonDetailUiModel {
            return PersonDetailUiModel(
                id = id,
                imagePath = null,
                name = "",
                knownForDepartment = "",
                birthday = "",
                deathday = "",
                placeOfBirth = "",
                biography = "",
                castMovies = emptyList(),
                crewMovies = emptyList(),
                castTvShows = emptyList(),
                crewTvShows = emptyList(),
            )
        }
    }
}
