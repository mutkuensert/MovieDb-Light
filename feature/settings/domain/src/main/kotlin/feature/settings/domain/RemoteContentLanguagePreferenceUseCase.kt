package feature.settings.domain

import core.domain.common.listener.RemoteContentLanguagePreferenceChangeListener

class RemoteContentLanguagePreferenceUseCase(
    private val settingsRepository: SettingsRepository,
    private val remoteContentLanguagePreferenceChangeListeners: List<RemoteContentLanguagePreferenceChangeListener>
) {
    fun setLanguage(language: String) {
        settingsRepository.setContentLanguage(language)
        remoteContentLanguagePreferenceChangeListeners.forEach { it.onRemoteContentLanguagePreferenceChanged() }
    }
}