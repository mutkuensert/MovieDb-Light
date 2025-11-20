package feature.settings.domain

interface SettingsRepository {
    fun setContentLanguage(language: String)
    fun getContentLanguage(): String
}