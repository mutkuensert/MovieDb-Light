package core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import core.ui.R
import core.ui.coil.debugPlaceholder
import libraries.image.TmdbImage

@Composable
fun Poster(
    modifier: Modifier = Modifier,
    imagePath: String?,
    imageType: ImageType = ImageType.POSTER,
    posterHeight: PosterHeight = PosterHeight.Large,
    imageQuality: ImageQuality? = null,
    contentScale: ContentScale = ContentScale.FillHeight,
    onSuccess: (AsyncImagePainter.State.Success) -> Unit = {},
    onError: (AsyncImagePainter.State.Error) -> Unit = {}
) {
    val imageSizeModifier = when (posterHeight) {
        PosterHeight.MaxHeight -> Modifier.fillMaxHeight()
        else -> Modifier.height(posterHeight.height)
    }

    Box(modifier) {
        var loading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf(false) }
        var currentImageUrl by remember {
            mutableStateOf(imagePath?.let {
                TmdbImage(it).createImageUrl(imageType, posterHeight, imageQuality)
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
            modifier = imageSizeModifier
                .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium),
            contentDescription = stringResource(R.string.image),
            contentScale = contentScale
        )

        if (loading) {
            Box(
                modifier = imageSizeModifier.width(posterHeight.estimatedWidth),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        }

        if (error && !LocalInspectionMode.current) {
            Box(
                modifier = imageSizeModifier
                    .width(posterHeight.estimatedWidth)
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
    posterHeight: PosterHeight,
    imageQuality: ImageQuality? = null,
): String {
    return when (imageType) {
        ImageType.POSTER -> {
            if (imageQuality != null) {
                when (imageQuality) {
                    ImageQuality.LOW -> this.poster.w342Url
                    ImageQuality.MEDIUM -> this.poster.w500Url
                    ImageQuality.HIGH -> this.poster.w780Url
                    ImageQuality.ORIGINAL -> this.originalSizedUrl
                }
            } else {
                when (posterHeight) {
                    PosterHeight.Small -> this.poster.w500Url
                    PosterHeight.Medium -> this.poster.w780Url
                    PosterHeight.Large -> this.poster.w780Url
                    else -> this.originalSizedUrl
                }
            }
        }

        ImageType.PROFILE -> {
            if (imageQuality != null) {
                when (imageQuality) {
                    ImageQuality.LOW -> this.profile.w185Url
                    ImageQuality.MEDIUM -> this.profile.h632Url
                    ImageQuality.HIGH -> this.profile.h632Url
                    ImageQuality.ORIGINAL -> this.originalSizedUrl
                }
            } else {
                when (posterHeight) {
                    PosterHeight.Small -> this.profile.h632Url
                    PosterHeight.Medium -> this.profile.h632Url
                    else -> this.originalSizedUrl
                }
            }
        }

        ImageType.LOGO -> {
            if (imageQuality != null) {
                when (imageQuality) {
                    ImageQuality.LOW -> this.logo.w92Url
                    ImageQuality.MEDIUM -> this.logo.w300Url
                    ImageQuality.HIGH -> this.logo.w500Url
                    ImageQuality.ORIGINAL -> this.originalSizedUrl
                }
            } else {
                when (posterHeight) {
                    PosterHeight.Small -> this.logo.w300Url
                    PosterHeight.Medium -> this.logo.w500Url
                    else -> this.originalSizedUrl
                }
            }
        }
    }
}

enum class PosterHeight(val height: Dp) {
    Unspecified(Dp.Unspecified),
    Small(80.dp),
    Medium(160.dp),
    Large(240.dp),
    ExtraLarge(320.dp),
    MaxHeight((-1).dp);

    val estimatedWidth: Dp
        get() = if (height != (-1).dp && height != Dp.Unspecified) {
            height * 2 / 3
        } else {
            Dp.Unspecified
        }
}

enum class ImageType {
    POSTER, PROFILE, LOGO
}

enum class ImageQuality {
    LOW, MEDIUM, HIGH, ORIGINAL
}
