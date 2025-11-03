package core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import core.ui.R
import core.ui.coil.debugPlaceholder

@Composable
fun Poster(
    modifier: Modifier = Modifier,
    url: String?,
    size: PosterSize = PosterSize.Medium,
    onSuccess: (AsyncImagePainter.State.Success) -> Unit = {}
) {
    val sizeModifier = when (size) {
        PosterSize.MaxWidth -> Modifier.fillMaxWidth()
        PosterSize.MaxHeight -> Modifier.fillMaxHeight()
        else -> Modifier.height(size.height)
    }

    Box {
        var loading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf(false) }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .allowHardware(true)
                .build(),
            onSuccess = {
                loading = false
                onSuccess(it)
            },
            onError = {
                loading = false
                error = true
            },
            error = debugPlaceholder(R.drawable.debug_placeholder_dog),
            modifier = modifier
                .then(sizeModifier)
                .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium),
            contentDescription = stringResource(R.string.image)
        )

        if (loading) {
            Box(
                modifier = sizeModifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        }

        if (error) {
            Box(
                modifier = Modifier
                    .then(sizeModifier)
                    .background(Color.Black)
            )
        }
    }
}

enum class PosterSize(val height: Dp) {
    Small(80.dp), Medium(160.dp), Big(240.dp), ExtraBig(320.dp), MaxWidth((-1).dp), MaxHeight((-1).dp)
}