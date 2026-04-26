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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val totalItemWidth = indicatorSize + spacing
    val boxWidth = (visibleDotsCount * (indicatorSize + spacing)) - spacing
    var targetOffset by remember { mutableStateOf(0.dp) }
    val offset = animateDpAsState(targetOffset)
    var previousIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentIndex) {
        if (currentIndex < visibleDotsCount - 2) {
            targetOffset = 0.dp
            return@LaunchedEffect
        }
        var offsetDiff = if (currentIndex > previousIndex) {
            totalItemWidth * -1
        } else {
            totalItemWidth
        }

        if (currentIndex == totalCount - 1) {
            offsetDiff -= spacing
        }
        targetOffset += offsetDiff
        previousIndex = currentIndex
    }

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
                                    scaleX = 0.5f
                                    scaleY = 0.5f
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