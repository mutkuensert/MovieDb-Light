package feature.settings.presentation

data class SettingsUiModel(
    val languagePreference: String,
) {
    companion object {
        fun initial(): SettingsUiModel {
            return SettingsUiModel(languagePreference = "")
        }
    }
}
