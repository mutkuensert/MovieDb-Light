package feature.person.data

import com.github.michaelbull.result.Result
import core.data.network.mapToDomain
import core.data.util.withDecimals
import core.database.LanguagePreference
import core.domain.Failure
import feature.person.data.remote.PersonService
import feature.person.data.remote.response.PersonMovieCastDto
import feature.person.data.remote.response.PersonMovieCrewDto
import feature.person.data.remote.response.PersonTvCastDto
import feature.person.data.remote.response.PersonTvCrewDto
import feature.person.domain.PersonRepository
import feature.person.domain.model.PersonDetails
import feature.person.domain.model.PersonMovieCredit
import feature.person.domain.model.PersonMovieCredits
import feature.person.domain.model.PersonTvCredit
import feature.person.domain.model.PersonTvCredits

class PersonRepositoryImpl(
    private val personService: PersonService,
    private val languagePreference: LanguagePreference,
) : PersonRepository {

    override suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure> {
        return personService.getPersonDetails(
            personId,
            languagePreference.getLanguageTag()
        ).mapToDomain { response ->
            PersonDetails(
                id = response.id,
                name = response.name,
                imagePath = response.profilePath,
                biography = response.biography,
                birthday = response.birthday,
                deathday = response.deathday,
                placeOfBirth = response.placeOfBirth,
                knownForDepartment = response.knownForDepartment
            )
        }
    }

    override suspend fun getPersonMovieCredits(
        personId: Int
    ): Result<PersonMovieCredits, Failure> {
        return personService.getPersonMovieCredits(
            personId,
            languagePreference.getLanguageTag()
        ).mapToDomain { response ->
            PersonMovieCredits(
                cast = response.cast?.toCastMovieCredits() ?: emptyList(),
                crew = response.crew?.toCrewMovieCredits() ?: emptyList()
            )
        }
    }

    override suspend fun getPersonTvCredits(
        personId: Int
    ): Result<PersonTvCredits, Failure> {
        return personService.getPersonTvCredits(
            personId,
            languagePreference.getLanguageTag()
        ).mapToDomain { response ->
            PersonTvCredits(
                cast = response.cast?.toCastTvCredits() ?: emptyList(),
                crew = response.crew?.toCrewTvCredits() ?: emptyList()
            )
        }
    }

    private fun List<PersonMovieCastDto>.toCastMovieCredits(): List<PersonMovieCredit> {
        return distinctBy { it.id }
            .sortedWith(
                compareByDescending<PersonMovieCastDto> {
                    it.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                }.thenByDescending { it.popularity }
            )
            .mapNotNull { movie ->
                val title = movie.title ?: movie.originalTitle ?: return@mapNotNull null
                PersonMovieCredit(
                    id = movie.id,
                    title = title,
                    imagePath = movie.posterPath,
                    voteAverage = movie.voteAverage?.withDecimals(1),
                    releaseDate = movie.releaseDate,
                    character = movie.character,
                    job = null
                )
            }
    }

    private fun List<PersonMovieCrewDto>.toCrewMovieCredits(): List<PersonMovieCredit> {
        return groupBy { it.id }
            .values
            .mapNotNull { credits ->
                val movie = credits.first()
                val title = movie.title ?: movie.originalTitle ?: return@mapNotNull null
                PersonMovieCredit(
                    id = movie.id,
                    title = title,
                    imagePath = movie.posterPath,
                    voteAverage = movie.voteAverage?.withDecimals(1),
                    releaseDate = movie.releaseDate,
                    character = null,
                    job = credits.mapNotNull { it.job }.distinct().joinToString(", ")
                )
            }
            .sortedWith(
                compareByDescending<PersonMovieCredit> {
                    it.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                }.thenByDescending { movie ->
                    find { it.id == movie.id }?.popularity
                }
            )
    }

    private fun List<PersonTvCastDto>.toCastTvCredits(): List<PersonTvCredit> {
        return distinctBy { it.id }
            .sortedWith(
                compareByDescending<PersonTvCastDto> {
                    it.firstAirDate?.split("-")?.firstOrNull()?.toIntOrNull()
                }.thenByDescending { it.popularity }
            )
            .mapNotNull { tvShow ->
                val title = tvShow.name ?: tvShow.originalName ?: return@mapNotNull null
                PersonTvCredit(
                    id = tvShow.id,
                    title = title,
                    imagePath = tvShow.posterPath,
                    voteAverage = tvShow.voteAverage?.withDecimals(1),
                    releaseDate = tvShow.firstAirDate,
                    character = tvShow.character,
                    job = null
                )
            }
    }

    private fun List<PersonTvCrewDto>.toCrewTvCredits(): List<PersonTvCredit> {
        return groupBy { it.id }
            .values
            .mapNotNull { credits ->
                val tvShow = credits.first()
                val title = tvShow.name ?: tvShow.originalName ?: return@mapNotNull null
                PersonTvCredit(
                    id = tvShow.id,
                    title = title,
                    imagePath = tvShow.posterPath,
                    voteAverage = tvShow.voteAverage?.withDecimals(1),
                    releaseDate = tvShow.firstAirDate,
                    character = null,
                    job = credits.mapNotNull { it.job }.distinct().joinToString(", ")
                )
            }
            .sortedWith(
                compareByDescending<PersonTvCredit> {
                    it.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                }.thenByDescending { movie ->
                    find { it.id == movie.id }?.popularity
                }
            )
    }
}
