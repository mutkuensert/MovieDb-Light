package feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.settings.domain.SettingsRepository
import feature.settings.domain.usecase.SetRemoteContentLanguagePreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val setRemoteContentLanguagePreferenceUseCase: SetRemoteContentLanguagePreferenceUseCase,
) : ViewModel() {
    private val _uiModel = MutableStateFlow(SettingsUiModel.initial())
    val uiModel = _uiModel.asStateFlow()

    fun loadLanguagePreference() {
        viewModelScope.launch {
            val language = settingsRepository.getContentLanguage()
            _uiModel.update { it.copy(language = language) }
        }
    }

    fun handleLanguageClick(language: Locale) {
        viewModelScope.launch {
            setRemoteContentLanguagePreferenceUseCase(language)
            _uiModel.update { it.copy(language = language) }
        }
    }
}