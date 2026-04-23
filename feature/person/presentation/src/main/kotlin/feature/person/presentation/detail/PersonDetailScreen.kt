package feature.person.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.ui.AppColors
import core.ui.MoviedbLightTheme
import core.ui.StatusBarColorHandler
import core.ui.component.ImageType
import core.ui.component.OneTimeEffect
import core.ui.component.Poster
import core.ui.component.PosterHeight
import core.ui.darkenBy
import feature.person.presentation.R
import feature.person.presentation.detail.model.MovieUiModel
import feature.person.presentation.detail.model.PersonDetailUiModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonDetailScreen(viewModel: PersonDetailViewModel = koinViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    PersonDetail(
        uiModel,
        viewModel::handleMovieClick
    )

    StatusBarColorHandler(MaterialTheme.colorScheme.background)

    OneTimeEffect {
        viewModel.getDetails()
    }
}

@Composable
private fun PersonDetail(
    uiModel: PersonDetailUiModel,
    onClickMovie: (id: Int) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Header(uiModel, Modifier.padding(horizontal = 16.dp, vertical = 16.dp))

            Column(Modifier.padding(horizontal = 16.dp)) {
                if (uiModel.biography.isNotBlank()) {
                    Biography(uiModel.biography)
                }
            }

            Spacer(Modifier.height(16.dp))

            Movies(
                title = stringResource(R.string.acted_movies),
                movies = uiModel.castMovies,
                onClickMovie = onClickMovie
            )

            Movies(
                title = stringResource(R.string.crew_movies),
                movies = uiModel.crewMovies,
                onClickMovie = onClickMovie
            )
        }
    }
}

@Composable
private fun Header(
    uiModel: PersonDetailUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Poster(
            modifier = Modifier
                .width(160.dp)
                .clip(MaterialTheme.shapes.medium),
            imagePath = uiModel.imagePath,
            imageType = ImageType.PROFILE
        )

        Column(Modifier.weight(1f)) {
            Text(
                text = uiModel.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            PersonInfo(uiModel, Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
private fun PersonInfo(
    uiModel: PersonDetailUiModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (uiModel.knownForDepartment.isNotBlank()) {
            Text(
                text = uiModel.knownForDepartment,
                color = MaterialTheme.colorScheme.onBackground.darkenBy(30),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiModel.birthday.isNotBlank()) {
            Text(
                text = stringResource(R.string.born, uiModel.birthday),
                color = MaterialTheme.colorScheme.onBackground.darkenBy(30),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiModel.deathday.isNotBlank()) {
            Text(
                text = stringResource(R.string.died, uiModel.deathday),
                color = MaterialTheme.colorScheme.onBackground.darkenBy(30),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiModel.placeOfBirth.isNotBlank()) {
            Text(
                text = stringResource(R.string.place_of_birth, uiModel.placeOfBirth),
                color = MaterialTheme.colorScheme.onBackground.darkenBy(30),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun Biography(biography: String, modifier: Modifier = Modifier) {
    var collapsed by remember { mutableStateOf(true) }

    Box(modifier.clickable {
        collapsed = !collapsed
    }) {
        Text(
            modifier = Modifier
                .then(if (collapsed) Modifier.height(92.dp) else Modifier),
            text = biography,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        if (collapsed) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .align(Alignment.BottomEnd)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            ),
                        )
                    )
            )
        }
    }
}

@Composable
private fun Movies(
    title: String,
    movies: List<MovieUiModel>,
    onClickMovie: (movieId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (movies.isEmpty()) {
        return
    }

    Column(modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(320.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
        ) {
            items(movies.size) { index ->
                Movie(
                    movies[index],
                    onClickMovie,
                    Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun Movie(
    movie: MovieUiModel,
    onClickMovie: (movieId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable { onClickMovie(movie.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(
            modifier = Modifier.height(PosterHeight.large),
            imagePath = movie.imagePath
        )

        Spacer(Modifier.height(6.dp))

        if (movie.year.isNotBlank() || movie.voteAverage != null) {
            MovieMetadata(movie, Modifier.padding(top = 2.dp))
        }

        val creditText = when {
            movie.character.isNotBlank() -> stringResource(R.string.as_character, movie.character)
            movie.job.isNotBlank() -> movie.job
            else -> null
        }

        if (creditText != null) {
            Text(
                text = creditText,
                modifier = Modifier
                    .width(120.dp)
                    .padding(top = 2.dp),
                color = MaterialTheme.colorScheme.onBackground.darkenBy(30),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MovieMetadata(
    movie: MovieUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (movie.year.isNotBlank()) {
            Text(
                text = movie.year,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (movie.voteAverage != null) {
            if (movie.year.isNotBlank()) {
                Spacer(Modifier.width(8.dp))
            }
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Filled.Star,
                tint = AppColors.star,
                contentDescription = stringResource(core.ui.R.string.vote_icon)
            )
            Text(
                text = movie.voteAverage,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun PersonDetailPreview() {
    MoviedbLightTheme {
        val movies = listOf(
            MovieUiModel(
                id = 1,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            MovieUiModel(
                id = 2,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            MovieUiModel(
                id = 3,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            MovieUiModel(
                id = 4,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            MovieUiModel(
                id = 5,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
        )
        val uiModel = PersonDetailUiModel(
            id = 0,
            imagePath = null,
            name = "Harry Potter",
            knownForDepartment = "Student",
            birthday = "1995",
            deathday = "-",
            placeOfBirth = "England",
            biography = "",
            castMovies = movies,
            crewMovies = movies
        )
        PersonDetail(uiModel, onClickMovie = {})
    }

}