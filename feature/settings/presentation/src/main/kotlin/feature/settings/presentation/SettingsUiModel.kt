package feature.settings.presentation

data class SettingsUiModel(
    val language: String,
) {
    companion object {
        fun initial(): SettingsUiModel {
            return SettingsUiModel(language = "")
        }
    }
}
