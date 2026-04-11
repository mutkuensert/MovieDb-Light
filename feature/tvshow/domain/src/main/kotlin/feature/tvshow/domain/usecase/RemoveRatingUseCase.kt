package feature.tvshow.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.Failure
import core.domain.profile.RatedTvShowsRefresher
import feature.tvshow.domain.TvShowRepository

class RemoveRatingUseCase(
    private val tvShowRepository: TvShowRepository,
    private val ratedTvShowsRefresher: RatedTvShowsRefresher,
) {
    suspend operator fun invoke(tvShowId: Int): Result<Unit, Failure> {
        return tvShowRepository.removeRating(tvShowId).onSuccess {
            ratedTvShowsRefresher()
        }
    }
}
