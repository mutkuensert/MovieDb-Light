package core.database

import android.content.Context
import timber.log.Timber
import utils.LocalizationHelper
import java.util.Locale

private const val KEY_LANGUAGE_TAG = "language-tag"

class LanguagePreference(context: Context) {
    private val preferences = context.getSharedPreferences(
        "language_preference",
        Context.MODE_PRIVATE
    )

    fun set(language: Locale): Boolean {
        val languageTag = language.toLanguageTag()
        val isValidTag = LocalizationHelper.isValidTag(languageTag)
        if (!isValidTag) {
            Timber.w("language tag $languageTag is not valid.")
            return false
        }
        return preferences.edit().putString(KEY_LANGUAGE_TAG, languageTag).commit()
    }

    fun get(): Locale {
        val tag = preferences.getString(KEY_LANGUAGE_TAG, null) ?: "en-US"
        return Locale.forLanguageTag(tag)
    }
}
