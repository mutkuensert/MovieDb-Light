package core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import core.ui.R
import core.ui.TmdbImage
import core.ui.coil.debugPlaceholder

@Composable
fun Poster(
    modifier: Modifier = Modifier,
    imagePath: String?,
    imageType: ImageType = ImageType.POSTER,
    imageQuality: ImageQuality = ImageQuality.MEDIUM,
    contentScale: ContentScale = ContentScale.Crop,
    aspectRatio: Float = 2f / 3f,
    onSuccess: (AsyncImagePainter.State.Success) -> Unit = {},
    onError: (AsyncImagePainter.State.Error) -> Unit = {}
) {
    Box(modifier.aspectRatio(aspectRatio)) {
        var loading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf(false) }
        var currentImageUrl by remember {
            mutableStateOf(imagePath?.let {
                TmdbImage(it).createImageUrl(imageType, imageQuality)
            })
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentImageUrl)
                .listener(
                    onError = { _, _ ->
                        val _currentImageUrl = currentImageUrl ?: return@listener
                        if (imagePath == null) return@listener
                        val originalSizedUrl = TmdbImage(imagePath).originalSizedUrl
                        if (_currentImageUrl != originalSizedUrl) {
                            currentImageUrl = originalSizedUrl
                        }
                    }
                )
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
                onError(it)
            },
            error = debugPlaceholder(R.drawable.debug_placeholder_dog),
            modifier = Modifier
                .matchParentSize()
                .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium),
            contentDescription = stringResource(R.string.image),
            contentScale = contentScale
        )

        if (loading) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        }

        if (error && !LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.width(36.dp),
                    imageVector = Icons.Filled.NoPhotography,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.no_image_icon)
                )
            }
        }
    }
}

private fun TmdbImage.createImageUrl(
    imageType: ImageType,
    imageQuality: ImageQuality
): String {
    return when (imageType) {
        ImageType.POSTER -> {
            when (imageQuality) {
                ImageQuality.LOW -> this.poster.w342Url
                ImageQuality.MEDIUM -> this.poster.w500Url
                ImageQuality.HIGH -> this.poster.w780Url
                ImageQuality.ORIGINAL -> this.originalSizedUrl
            }
        }

        ImageType.PROFILE -> {
            when (imageQuality) {
                ImageQuality.LOW -> this.profile.w185Url
                ImageQuality.MEDIUM -> this.profile.h632Url
                ImageQuality.HIGH -> this.profile.h632Url
                ImageQuality.ORIGINAL -> this.originalSizedUrl
            }

        }

        ImageType.LOGO -> {
            when (imageQuality) {
                ImageQuality.LOW -> this.logo.w92Url
                ImageQuality.MEDIUM -> this.logo.w300Url
                ImageQuality.HIGH -> this.logo.w500Url
                ImageQuality.ORIGINAL -> this.originalSizedUrl
            }
        }
    }
}

object PosterHeight {
    val small = 80.dp
    val medium = 160.dp
    val large = 240.dp
    val extraLarge = 320.dp
}

enum class ImageType {
    POSTER, PROFILE, LOGO
}

enum class ImageQuality {
    LOW, MEDIUM, HIGH, ORIGINAL
}
