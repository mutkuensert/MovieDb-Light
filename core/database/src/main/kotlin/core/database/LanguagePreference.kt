package core.database

import android.content.Context
import libraries.LocalizationHelper
import timber.log.Timber

private const val KEY_LANGUAGE_TAG = "language-tag"

class LanguagePreference(context: Context) {
    private val preferences = context.getSharedPreferences(
        "language_preference",
        Context.MODE_PRIVATE
    )

    fun setByTag(languageTag: String): Boolean {
        val isValidTag = LocalizationHelper.validateTag(languageTag)
        if (!isValidTag) {
            Timber.w("language tag $languageTag is not valid.")
            return false
        }
        return preferences.edit().putString(KEY_LANGUAGE_TAG, languageTag).commit()
    }

    fun getLanguageTag(): String {
        return preferences.getString(KEY_LANGUAGE_TAG, null) ?: "en-US"
    }
}
