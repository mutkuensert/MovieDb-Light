package core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp

@Composable
fun CollapsableText(
    text: String,
    modifier: Modifier = Modifier,
    characterLimitThreshold: Int = 200,
    moreToReadIndicator: String = "...",
) {
    var collapsed by remember { mutableStateOf(true) }

    Box(modifier.clickable(enabled = text.length > characterLimitThreshold) {
        collapsed = !collapsed
    }) {
        val isOverviewTooLong = text.length > characterLimitThreshold
        Text(
            modifier = Modifier
                .then(if (collapsed && isOverviewTooLong) Modifier.height(92.dp) else Modifier),
            text = if (isOverviewTooLong && collapsed) {
                text.substring(0, characterLimitThreshold) + moreToReadIndicator
            } else {
                text
            },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (collapsed && isOverviewTooLong) {
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

@Preview
@Composable
private fun CollapsableTextPreview() {
    CollapsableText(LoremIpsum(100).values.joinToString { " " })
}