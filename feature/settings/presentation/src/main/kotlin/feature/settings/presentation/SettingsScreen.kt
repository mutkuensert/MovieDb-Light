package feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            Modifier
                .padding(top = 16.dp)
                .background(Color.Transparent, MaterialTheme.shapes.medium)
        ) {
            Text("Language:")
            Spacer(Modifier.width(4.dp))
            Text("en-US")
        }
    }
}