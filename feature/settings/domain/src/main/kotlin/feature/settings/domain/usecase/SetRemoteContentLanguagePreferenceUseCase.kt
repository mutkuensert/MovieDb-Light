package feature.settings.domain.usecase

import javax.inject.Inject

import core.domain.common.LanguageRelatedDataRefresher
import feature.settings.domain.SettingsRepository

class SetRemoteContentLanguagePreferenceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val languageRelatedDataRefreshers: Set<@JvmSuppressWildcards LanguageRelatedDataRefresher>,
) {
    suspend operator fun invoke(language: String) {
        settingsRepository.setContentLanguage(language)
        languageRelatedDataRefreshers.forEach { it.invoke() }
    }
}
