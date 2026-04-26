package core.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlin.math.max
import kotlin.math.min

@Composable
fun PageIndicator(
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.White,
    indicatorSize: Dp = 16.dp,
    spacing: Dp = 6.dp,
    visibleDotsCount: Int = 5
) {
    val fraction = 0.5f
    val totalItemWidth = indicatorSize + spacing
    val displayedDotsCount = min(totalCount, visibleDotsCount)
    val boxWidth = (displayedDotsCount * totalItemWidth) - spacing + indicatorSize * fraction
    val centeredIndex = visibleDotsCount / 2
    val maxScrollableSteps = max(totalCount - visibleDotsCount, 0)
    val scrollSteps = (currentIndex - centeredIndex).coerceIn(0, maxScrollableSteps)
    val targetOffset = -(totalItemWidth * scrollSteps)
    val offset = animateDpAsState(targetOffset)

    Box(modifier) {
        Box(
            modifier = Modifier
                .width(boxWidth)
                .height(indicatorSize)
                .clipToBounds(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.Start, unbounded = true)
                    .offset(offset = {
                        IntOffset(x = offset.value.toPx().toInt(), y = 0)
                    }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                repeat(totalCount) { iterationIndex ->
                    val color = if (currentIndex == iterationIndex) activeColor else inactiveColor

                    Box(
                        modifier = Modifier
                            .size(indicatorSize)
                            .graphicsLayer {
                                if (currentIndex != iterationIndex) {
                                    scaleX = fraction
                                    scaleY = fraction
                                }
                            }
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
        }
    }
}
