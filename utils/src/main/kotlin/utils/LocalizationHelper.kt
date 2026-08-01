package utils

import java.util.Locale

object LocalizationHelper {
    private val locales = Locale.getAvailableLocales()
    val systemCountry: String get() = Locale.getDefault().country
    val systemLanguage: Locale get() = Locale.getDefault()
    val availableLanguages: List<Locale> = locales.groupBy { it.language }
        .mapNotNull { (_, locales) ->
            locales.minByOrNull { it.toLanguageTag().length }
        }

    fun isValidTag(tag: String): Boolean {
        return locales.map { it.toLanguageTag() }.contains(tag)
    }
}
