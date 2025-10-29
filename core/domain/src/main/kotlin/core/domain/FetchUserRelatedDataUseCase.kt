package core.domain

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import timber.log.Timber

class FetchUserRelatedDataUseCase(private val accountRepository: AccountRepository) {

    suspend fun execute() {
        accountRepository.fetchAccountDetails().onSuccess {
            accountRepository.fetchFavoriteMovies()
        }.onFailure {
            Timber.w("User")
        }
    }
}