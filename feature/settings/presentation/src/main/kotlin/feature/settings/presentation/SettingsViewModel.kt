package feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.settings.domain.SetRemoteContentLanguagePreferenceUseCase
import feature.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
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

    fun handleLanguageClick(language: String) {
        viewModelScope.launch {
            setRemoteContentLanguagePreferenceUseCase(language)
            _uiModel.update { it.copy(language = language) }
        }
    }
}