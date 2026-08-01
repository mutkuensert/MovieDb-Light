package feature.settings.presentation

import java.util.Locale

data class SettingsUiModel(
    val language: Locale,
) {
    companion object {
        fun initial(): SettingsUiModel {
            return SettingsUiModel(language = Locale.getDefault())
        }
    }
}
