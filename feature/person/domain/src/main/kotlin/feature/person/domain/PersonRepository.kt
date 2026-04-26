package feature.person.domain

import com.github.michaelbull.result.Result
import core.domain.Failure
import feature.person.domain.model.PersonDetails
import feature.person.domain.model.PersonMovieCredits
import feature.person.domain.model.PersonTvCredits

interface PersonRepository {
    suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure>
    suspend fun getPersonMovieCredits(personId: Int): Result<PersonMovieCredits, Failure>
    suspend fun getPersonTvCredits(personId: Int): Result<PersonTvCredits, Failure>
}
