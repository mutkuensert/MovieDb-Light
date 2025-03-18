package core.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.toBitmap
import core.ui.AppColors
import core.ui.R
import core.ui.palette.bodyTextColor
import core.ui.palette.titleTextColor

@Composable
fun InteractivePoster(
    modifier: Modifier = Modifier,
    url: String?,
    size: ImageSize = ImageSize.Big,
    title: String = "",
    description: String? = null,
    vote: String? = null,
    onClick: () -> Unit
) {
    var infoColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var textColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var isInfoVisible by remember { mutableStateOf(false) }
    val blurModifier: Modifier = if (isInfoVisible) {
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .blur(10.dp)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        val density = LocalDensity.current
        var imageWidth by remember { mutableStateOf(0.dp) }
        Image(
            modifier = Modifier
                .then(blurModifier)
                .onGloballyPositioned {
                    imageWidth = with(density) { it.size.width.toDp() }
                },
            url = url,
            size = size,
            onSuccess = { state ->
                val bitmap = state.result.image.toBitmap().copy(Bitmap.Config.ARGB_8888, true)
                infoColor = bitmap.titleTextColor
                textColor = bitmap.bodyTextColor
            }
        )

        InfoIcon(infoColor) { isInfoVisible = !isInfoVisible }

        if (isInfoVisible) {
            InfoView(imageWidth, title, textColor, vote, description)
        }
    }
}

@Composable
private fun BoxScope.InfoView(
    imageWidth: Dp,
    title: String,
    textColor: Color,
    vote: String?,
    description: String?
) {
    Column(
        Modifier
            .width(imageWidth)
            .padding(horizontal = 20.dp)
            .align(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = textColor,
            overflow = TextOverflow.Visible,
            style = MaterialTheme.typography.bodyLarge
        )

        if (vote != null) {
            VoteView(textColor, vote)
        }

        if (description != null) {
            DescriptionView(textColor, description)
        }
    }
}

@Composable
private fun DescriptionView(textColor: Color, description: String) {
    Column(
        modifier = Modifier
            .height(40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            color = textColor,
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
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
private fun BoxScope.InfoIcon(color: Color, onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .padding(16.dp)
            .size(30.dp)
            .align(Alignment.TopEnd)
            .clickable { onClick.invoke() },
        imageVector = Icons.Default.Info,
        tint = color,
        contentDescription = stringResource(R.string.info)
    )
}