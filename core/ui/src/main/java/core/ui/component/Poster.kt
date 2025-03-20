package core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import core.ui.R

@Composable
fun Poster(
    modifier: Modifier = Modifier,
    url: String?,
    size: ImageSize = ImageSize.Medium,
    onSuccess: (AsyncImagePainter.State.Success) -> Unit = {}
) {
    val sizeModifier = when (size) {
        ImageSize.MaxWidth -> Modifier.fillMaxWidth()
        ImageSize.MaxHeight -> Modifier.fillMaxHeight()
        else -> Modifier.height(size.height)
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .allowHardware(true)
            .build(),
        loading = {
            Box(
                modifier = Modifier
                    .then(if (size != ImageSize.MaxWidth) sizeModifier else Modifier)
                    .width(160.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        },
        onSuccess = onSuccess,
        error = {
            Box(
                modifier = Modifier
                    .then(if (size != ImageSize.MaxWidth) sizeModifier else Modifier)
                    .width(160.dp)
                    .background(Color.Black)
            )
        },
        modifier = modifier
            .then(sizeModifier)
            .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium),
        contentDescription = stringResource(R.string.image)
    )
}

enum class ImageSize(val height: Dp) {
    Small(80.dp), Medium(160.dp), Big(240.dp), ExtraBig(320.dp), MaxWidth((-1).dp), MaxHeight((-1).dp)
}