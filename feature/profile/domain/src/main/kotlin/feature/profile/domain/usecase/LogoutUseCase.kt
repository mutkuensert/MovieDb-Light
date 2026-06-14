package feature.profile.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.auth.AuthenticationRepository

class LogoutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(): Result<Unit, Failure> {
        return authenticationRepository.logout().onOk {
            accountRepository.clearUserRelatedData()
        }
    }
}