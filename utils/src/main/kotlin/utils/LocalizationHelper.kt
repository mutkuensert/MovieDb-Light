package utils

import java.util.Locale

object LocalizationHelper {
    private val locales = Locale.getAvailableLocales()
    val systemCountry: String get() = Locale.getDefault().country
    val systemLanguage: String get() = Locale.getDefault().displayLanguage
    val systemLanguageTag: String get() = Locale.getDefault().toLanguageTag()
    val allCountries: Array<String> get() = Locale.getISOCountries()
    val availableLanguages = locales.groupBy { it.language }
        .mapNotNull { (_, locales) ->
            locales.minByOrNull { it.toLanguageTag().length }
        }.map { it.displayLanguage }

    fun getLocaleByLanguage(language: String): Locale {
        return locales.find {
            it.displayLanguage.equals(language, ignoreCase = true)
        }.requireNotNull("Language parameter is not valid.")
    }

    fun getLocaleByTag(tag: String): Locale {
        return locales.find {
            it.toLanguageTag().equals(tag, ignoreCase = true)
        }.requireNotNull("Tag parameter is not valid.")
    }

    fun validateTag(tag: String): Boolean {
        return locales.find {
            it.toLanguageTag().equals(tag, ignoreCase = true)
        } != null
    }
}
