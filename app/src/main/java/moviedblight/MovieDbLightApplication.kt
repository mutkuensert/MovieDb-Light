package moviedblight

import android.app.Application
import com.mutkuensert.moviedblight.BuildConfig
import core.data.dataModule
import core.database.databaseModule
import core.injection.feature.movies.moviesModule
import core.injection.feature.movies.profileModule
import core.libraries.librariesModule
import core.ui.uiModule
import moviedblight.ui.home.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MovieDbLightApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimber()
        startKoin {
            androidLogger()
            androidContext(this@MovieDbLightApplication)
            modules(
                homeModule,
                uiModule,
                dataModule,
                databaseModule,
                moviesModule,
                profileModule,
                librariesModule
            )
        }
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
