package feature.settings.domain

import java.util.Locale

interface SettingsRepository {
    fun setContentLanguage(language: Locale)
    fun getContentLanguage(): Locale
}