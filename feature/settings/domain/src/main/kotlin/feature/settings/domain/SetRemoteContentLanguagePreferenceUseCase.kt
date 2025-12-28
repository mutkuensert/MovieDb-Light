package feature.settings.domain

import core.domain.common.listener.RemoteContentLanguagePreferenceChangeListener

class SetRemoteContentLanguagePreferenceUseCase(
    private val settingsRepository: SettingsRepository,
    private val remoteContentLanguagePreferenceChangeListeners: List<RemoteContentLanguagePreferenceChangeListener>
) {
    operator fun invoke(language: String) {
        settingsRepository.setContentLanguage(language)
        remoteContentLanguagePreferenceChangeListeners.forEach { it.onRemoteContentLanguagePreferenceChanged() }
    }
}