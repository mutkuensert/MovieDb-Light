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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.ui.AppColors
import core.ui.FilmCanTheme
import core.ui.component.ImageType
import core.ui.component.OneTimeEffect
import core.ui.component.Poster
import core.ui.component.PosterHeight
import core.ui.darkenBy
import feature.person.presentation.R
import feature.person.presentation.detail.model.PersonDetailUiModel
import feature.person.presentation.detail.model.ProductionUiModel

@Composable
fun PersonDetailScreen(viewModel: PersonDetailViewModel = hiltViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    PersonDetail(
        uiModel,
        viewModel::handleMovieClick,
        viewModel::handleTvShowClick,
    )

    OneTimeEffect {
        viewModel.getDetails()
    }
}

@Composable
private fun PersonDetail(
    uiModel: PersonDetailUiModel,
    onClickMovie: (id: Int) -> Unit,
    onClickTvShow: (id: Int) -> Unit,
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

            Productions(
                title = stringResource(R.string.acted_movies),
                productions = uiModel.castMovies,
                onClickProduction = onClickMovie
            )

            Productions(
                title = stringResource(R.string.crew_movies),
                productions = uiModel.crewMovies,
                onClickProduction = onClickMovie
            )

            Productions(
                title = stringResource(R.string.acted_tv_shows),
                productions = uiModel.castTvShows,
                onClickProduction = onClickTvShow
            )

            Productions(
                title = stringResource(R.string.crew_tv_shows),
                productions = uiModel.crewTvShows,
                onClickProduction = onClickTvShow
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
private fun Productions(
    title: String,
    productions: List<ProductionUiModel>,
    onClickProduction: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (productions.isEmpty()) {
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
            items(productions.size) { index ->
                Production(
                    productions[index],
                    onClickProduction,
                    Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun Production(
    production: ProductionUiModel,
    onClickProduction: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable { onClickProduction(production.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(
            modifier = Modifier.height(PosterHeight.large),
            imagePath = production.imagePath
        )

        Spacer(Modifier.height(6.dp))

        if (production.year.isNotBlank() || production.voteAverage != null) {
            Metadata(production, Modifier.padding(top = 2.dp))
        }

        val creditText = when {
            production.character.isNotBlank() -> stringResource(
                R.string.as_character,
                production.character
            )

            production.job.isNotBlank() -> production.job
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
private fun Metadata(
    prodution: ProductionUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (prodution.year.isNotBlank()) {
            Text(
                text = prodution.year,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (prodution.voteAverage != null) {
            if (prodution.year.isNotBlank()) {
                Spacer(Modifier.width(8.dp))
            }
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Filled.Star,
                tint = AppColors.star,
                contentDescription = stringResource(core.ui.R.string.vote_icon)
            )
            Text(
                text = prodution.voteAverage,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun PersonDetailPreview() {
    FilmCanTheme {
        val productions = listOf(
            ProductionUiModel(
                id = 1,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            ProductionUiModel(
                id = 2,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            ProductionUiModel(
                id = 3,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            ProductionUiModel(
                id = 4,
                title = "Movie",
                imagePath = null,
                voteAverage = "10",
                year = "2000",
                character = "character",
                job = "job"
            ),
            ProductionUiModel(
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
            castMovies = productions,
            crewMovies = productions,
            castTvShows = productions,
            crewTvShows = productions,
        )
        PersonDetail(uiModel, onClickMovie = {}, onClickTvShow = {})
    }
}