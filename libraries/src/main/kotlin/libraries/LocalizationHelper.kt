package libraries

import java.util.Locale

object LocalizationHelper {
    val systemCountry: String get() = Locale.getDefault().country
    val systemLanguage: String get() = Locale.getDefault().language
    val systemLanguageTag: String get() = Locale.getDefault().toLanguageTag()
    val allCountries: Array<String> get() = Locale.getISOCountries()
    val languageTags = Locale.getAvailableLocales().filter {
        it.language.isNotBlank() && it.country.isNotBlank()
    }.distinctBy { it.toLanguageTag() }
}