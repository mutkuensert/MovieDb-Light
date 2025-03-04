package moviedblight

import android.app.Application
import core.data.network.networkModule
import core.database.databaseModule
import core.injection.feature.movies.moviesModule
import core.ui.uiModule
import moviedblight.ui.home.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MovieDbLight : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MovieDbLight)
            modules(
                homeModule,
                uiModule,
                networkModule,
                databaseModule,
                moviesModule
            )
        }
    }
}
