package feature.settings.domain

import core.domain.AppContentLanguageChangeListener

class SetContentLanguageUseCase(
    private val settingsRepository: SettingsRepository,
    private val appContentLanguageChangeListeners: List<AppContentLanguageChangeListener>
) {
    fun execute(language: String) {
        settingsRepository.setContentLanguage(language)
        appContentLanguageChangeListeners.forEach { it.onAppContentLanguageChanged() }
    }
}