package feature.settings.data

import core.database.LanguagePreference
import feature.settings.domain.SettingsRepository


class SettingsRepositoryImpl(
    private val languagePreference: LanguagePreference
) : SettingsRepository {

    override fun setLanguagePreference(languageTag: String) {
        languagePreference.set(languageTag)
    }

    override fun getLanguagePreference(): String {
        return languagePreference.getLanguageTag()
    }
}