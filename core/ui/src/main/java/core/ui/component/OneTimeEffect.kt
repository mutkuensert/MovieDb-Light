package core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun OneTimeEffect(block: () -> Unit) {
    var executed by rememberSaveable { mutableStateOf(false) }
    if (!executed) {
        executed = true
        block.invoke()
    }
}