package moviedblight.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import core.ui.LocalStatusBarBackgroundColorHandler
import core.ui.MoviedbLightTheme
import core.ui.StatusBarBackgroundColorHandler
import core.ui.navigation.Navigator
import moviedblight.ui.home.HomeScreen
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val navigator: Navigator by inject()
    private val statusBarBackgroundColorHandler: StatusBarBackgroundColorHandler by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalStatusBarBackgroundColorHandler provides statusBarBackgroundColorHandler) {
                MoviedbLightTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HomeScreen(navigator)
                    }
                }
            }
        }
    }
}
