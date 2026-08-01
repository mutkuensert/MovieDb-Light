package feature.settings.data

import core.database.LanguagePreference
import feature.settings.domain.SettingsRepository
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val languagePreference: LanguagePreference,
) : SettingsRepository {

    override fun setContentLanguage(language: Locale) {
        languagePreference.set(language)
    }

    override fun getContentLanguage(): Locale {
        return languagePreference.get()
    }
}