package feature.settings.domain.usecase

import core.domain.common.LanguageRelatedDataRefresher
import feature.settings.domain.SettingsRepository
import java.util.Locale
import javax.inject.Inject

class SetRemoteContentLanguagePreferenceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val languageRelatedDataRefreshers: Set<@JvmSuppressWildcards LanguageRelatedDataRefresher>,
) {
    suspend operator fun invoke(language: Locale) {
        settingsRepository.setContentLanguage(language)
        languageRelatedDataRefreshers.forEach { it.invoke() }
    }
}
