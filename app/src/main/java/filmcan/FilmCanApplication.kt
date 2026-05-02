package filmcan

import android.app.Application
import com.mutkuensert.filmcan.BuildConfig
import core.data.dataModule
import core.database.databaseModule
import core.domain.domainModule
import core.ui.uiModule
import feature.person.personModule
import feature.profile.profileModule
import feature.search.searchModule
import feature.settings.settingsModule
import movie.injection.movieModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber
import tvshow.injection.tvShowModule
import utils.utilsModule

class FilmCanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimber()
        startKoin {
            androidLogger()
            androidContext(this@FilmCanApplication)
            modules(
                appModule,
                databaseModule,
                dataModule,
                domainModule,
                uiModule,
                movieModule,
                tvShowModule,
                personModule,
                profileModule,
                searchModule,
                settingsModule,
                utilsModule
            )
        }
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
