package moviedblight

import android.app.Application
import com.mutkuensert.moviedblight.BuildConfig
import core.data.dataModule
import core.database.databaseModule
import core.injection.feature.movies.movieModule
import core.injection.feature.movies.profileModule
import core.libraries.librariesModule
import core.ui.uiModule
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
                appModule,
                databaseModule,
                dataModule,
                //domainModule,
                uiModule,
                movieModule,
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
