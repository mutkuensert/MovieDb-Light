package core.database

import android.content.Context
import androidx.core.content.edit
import core.domain.common.LanguagePreferenceUpdateState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import utils.LocalizationHelper
import java.util.Locale

private const val KEY_LANGUAGE_TAG = "language-tag"

class LanguagePreference(context: Context) : LanguagePreferenceUpdateState {
    private val preferences = context.getSharedPreferences(
        "language_preference",
        Context.MODE_PRIVATE
    )

    override val updatedLanguage = MutableSharedFlow<Locale>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    fun set(language: Locale) {
        val languageTag = language.toLanguageTag()
        val isValidTag = LocalizationHelper.isValidTag(languageTag)
        if (!isValidTag) {
            Timber.w("language tag $languageTag is not valid.")
            return
        }
        preferences.edit(commit = true) { putString(KEY_LANGUAGE_TAG, languageTag) }
        updatedLanguage.tryEmit(language)
    }

    fun get(): Locale {
        val tag = preferences.getString(KEY_LANGUAGE_TAG, null) ?: "en-US"
        return Locale.forLanguageTag(tag)
    }
}
