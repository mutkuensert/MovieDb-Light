package core.domain.common

interface LanguageRelatedDataRefresher {
    suspend operator fun invoke()
}