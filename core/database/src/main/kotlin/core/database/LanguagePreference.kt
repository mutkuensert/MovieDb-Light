package core.database

import android.content.Context

class LanguagePreference(context: Context) {
    private val preferences = context.getSharedPreferences(
        "language_preference", Context.MODE_PRIVATE
    )

    fun set(languageTag: String) {
        preferences.edit().putString("language-tag", languageTag).commit()
    }

    fun getLanguageTag(): String {
        return preferences.getString("language-tag", null) ?: "en-US"
    }
}