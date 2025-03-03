package core.ui.navigation.routes

import android.annotation.SuppressLint
import androidx.navigation.serialization.generateHashCode
import kotlinx.serialization.Serializable
import core.ui.navigation.NavTab

@Serializable
object MoviesRoute : NavTab {
    override val navDestinationId: Int
        @SuppressLint("RestrictedApi")
        get() = serializer().generateHashCode()
}