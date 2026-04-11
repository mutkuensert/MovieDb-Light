package feature.person.domain

import com.github.michaelbull.result.Result
import core.domain.Failure
import feature.person.domain.model.PersonDetails
import feature.person.domain.model.PersonMovieCredits

interface PersonRepository {
    suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure>
    suspend fun getPersonMovieCredits(personId: Int): Result<PersonMovieCredits, Failure>
}
