package feature.profile.presentation.profile

data class ProfileUiModel(val profileImageUrl: String?, val name: String) {
    companion object {
        fun empty(): ProfileUiModel {
            return ProfileUiModel(
                profileImageUrl = null,
                name = ""
            )
        }
    }
}
