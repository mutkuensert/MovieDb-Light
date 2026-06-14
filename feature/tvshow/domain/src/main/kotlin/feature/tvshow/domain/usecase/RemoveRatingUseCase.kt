package feature.tvshow.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.profile.RatedTvShowsRefresher
import feature.tvshow.domain.TvShowRepository

class RemoveRatingUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val ratedTvShowsRefresher: RatedTvShowsRefresher,
) {
    suspend operator fun invoke(tvShowId: Int): Result<Unit, Failure> {
        return tvShowRepository.removeRating(tvShowId).onOk {
            ratedTvShowsRefresher()
        }
    }
}
