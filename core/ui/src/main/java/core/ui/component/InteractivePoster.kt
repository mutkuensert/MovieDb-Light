package core.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Beenhere
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.toBitmap
import core.ui.AppColors
import core.ui.R
import core.ui.isDark
import core.ui.palette.bodyTextColor
import core.ui.palette.titleTextColor

@Composable
fun InteractivePoster(
    modifier: Modifier = Modifier,
    url: String?,
    size: PosterSize = PosterSize.Large,
    title: String = "",
    vote: String? = null,
    inWatchlist: Boolean? = null,
    onWatchlistClick: () -> Unit,
    onPosterClick: () -> Unit
) {
    var infoButtonColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var textColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    val watchlistButtonColor = if (inWatchlist == true) {
        Color(0xA6FFEB3B)
    } else {
        Color(0x80E1E1E1)
    }
    var isInfoVisible by remember { mutableStateOf(false) }

    val blurModifier: Modifier = if (isInfoVisible) {
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .blur(20.dp)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onPosterClick)
    ) {
        val density = LocalDensity.current
        var imageWidth by remember { mutableStateOf(0.dp) }
        Poster(
            modifier = Modifier
                .then(blurModifier)
                .onGloballyPositioned {
                    imageWidth = with(density) { it.size.width.toDp() }
                },
            url = url,
            posterSize = size,
            onSuccess = { state ->
                val bitmap = state.result.image.toBitmap().copy(Bitmap.Config.ARGB_8888, true)
                infoButtonColor = bitmap.titleTextColor
                textColor = bitmap.bodyTextColor
            }
        )

        if (isInfoVisible) {
            InfoView(
                Modifier
                    .height(size.height)
                    .width(imageWidth)
                    .align(Alignment.Center),
                title,
                textColor,
                vote
            )
        }

        InfoButton(infoButtonColor) { isInfoVisible = !isInfoVisible }

        if (inWatchlist != null) {
            WatchlistButton(watchlistButtonColor, onWatchlistClick)
        }
    }
}

@Composable
private fun InfoView(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color,
    vote: String?
) {
    val backgroundColorInContrastToText = if (textColor.isDark) {
        Color(0x99FFFFFF)
    } else {
        Color(0xB3000000)
    }

    Column(
        modifier
            .background(color = backgroundColorInContrastToText)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = textColor,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Visible,
            style = MaterialTheme.typography.bodyLarge
        )

        if (vote != null) {
            Spacer(Modifier.height(10.dp))
            VoteView(textColor, vote)
        }
    }
}

@Composable
private fun VoteView(textColor: Color, vote: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Filled.Star,
            tint = AppColors.star,
            contentDescription = stringResource(R.string.vote_icon)
        )

        Text(
            color = textColor,
            text = vote,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun BoxScope.InfoButton(color: Color, onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .padding(16.dp)
            .size(30.dp)
            .clip(CircleShape)
            .align(Alignment.TopEnd)
            .clickable { onClick.invoke() },
        imageVector = Icons.Default.Info,
        tint = color,
        contentDescription = stringResource(R.string.info)
    )
}

@Composable
private fun BoxScope.WatchlistButton(color: Color, onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .padding(16.dp)
            .size(30.dp)
            .align(Alignment.TopStart)
            .clickable { onClick.invoke() },
        imageVector = Icons.Default.Beenhere,
        tint = color,
        contentDescription = stringResource(R.string.watchlist_button)
    )
}