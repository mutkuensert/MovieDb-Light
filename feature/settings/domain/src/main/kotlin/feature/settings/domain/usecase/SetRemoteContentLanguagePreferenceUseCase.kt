package feature.settings.domain.usecase

import core.domain.common.LanguageRelatedDataRefresher
import feature.settings.domain.SettingsRepository

class SetRemoteContentLanguagePreferenceUseCase(
    private val settingsRepository: SettingsRepository,
    private val languageRelatedDataRefreshers: List<LanguageRelatedDataRefresher>,
) {
    suspend operator fun invoke(language: String) {
        settingsRepository.setContentLanguage(language)
        languageRelatedDataRefreshers.forEach { it.invoke() }
    }
}