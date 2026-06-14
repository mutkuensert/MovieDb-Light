package feature.settings.data

import javax.inject.Inject
import javax.inject.Singleton

import core.database.LanguagePreference
import feature.settings.domain.SettingsRepository
import utils.LocalizationHelper

@Singleton
class SettingsRepositoryImpl @Inject constructor(
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