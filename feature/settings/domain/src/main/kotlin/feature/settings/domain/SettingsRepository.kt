package feature.settings.domain

interface SettingsRepository {
    fun setLanguagePreference(languageTag: String)
    fun getLanguagePreference(): String
}