package feature.settings.data

import core.database.LanguagePreference
import feature.settings.domain.SettingsRepository
import libraries.LocalizationHelper

class SettingsRepositoryImpl(
    private val languagePreference: LanguagePreference,
) : SettingsRepository {

    override fun setContentLanguage(language: String) {
        languagePreference.setByTag(
            LocalizationHelper.getLocaleByLanguage(language).toLanguageTag()
        )
    }

    override fun getContentLanguage(): String {
        return LocalizationHelper.getLocaleByTag(languagePreference.getLanguageTag()).displayLanguage
    }
}