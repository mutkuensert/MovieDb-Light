package core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import core.ui.FilmCanTheme

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.primary,
    disabledTextColor: Color = MaterialTheme.colorScheme.primary,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    enabled: Boolean = true,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick,
        colors = colors,
        contentPadding = ButtonDefaults.ContentPadding,
        border = null,
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(),
        enabled = enabled,
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (enabled) textColor else disabledTextColor
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    FilmCanTheme { SecondaryButton(onClick = {}, text = "button text") }
}

@Preview
@Composable
private fun DisabledSecondaryButtonPreview() {
    FilmCanTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            SecondaryButton(
                onClick = {},
                text = "button text",
                enabled = false
            )
        }
    }
}