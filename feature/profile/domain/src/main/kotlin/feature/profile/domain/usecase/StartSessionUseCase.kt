package feature.profile.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.account.User
import core.domain.auth.AuthenticationRepository

class StartSessionUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(): Result<User, Failure> {
        return authenticationRepository.startSession().flatMap {
            accountRepository.getAccountDetails()
        }
    }
}
