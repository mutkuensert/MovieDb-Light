package moviedblight

import android.app.Application
import com.mutkuensert.moviedblight.BuildConfig
import core.data.dataModule
import core.database.databaseModule
import core.domain.domainModule
import core.ui.uiModule
import injection.movieModule
import injection.profileModule
import injection.settingsModule
import libraries.librariesModule
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
                domainModule,
                uiModule,
                movieModule,
                profileModule,
                settingsModule,
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
