package feature.person.data.remote

import core.data.DEFAULT_REMOTE_CONTENT_LANGUAGE
import core.data.network.NetworkResult
import feature.person.data.remote.response.PersonDetailsResponse
import feature.person.data.remote.response.PersonMovieCreditsResponse
import feature.person.data.remote.response.PersonTvCreditsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonService {

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<PersonDetailsResponse>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("person_id") personId: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<PersonMovieCreditsResponse>

    @GET("person/{person_id}/tv_credits")
    suspend fun getPersonTvCredits(
        @Path("person_id") personId: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<PersonTvCreditsResponse>
}
