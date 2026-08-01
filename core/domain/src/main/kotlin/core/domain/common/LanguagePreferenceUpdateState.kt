package core.domain.common

import kotlinx.coroutines.flow.SharedFlow
import java.util.Locale

interface LanguagePreferenceUpdateState {
    val updatedLanguage: SharedFlow<Locale>
}