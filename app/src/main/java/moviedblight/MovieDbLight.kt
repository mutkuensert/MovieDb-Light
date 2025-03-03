package moviedblight

import android.app.Application
import moviedblight.core.data.network.networkModule
import moviedblight.core.injection.commonmodules.core.dataModule
import moviedblight.core.injection.commonmodules.core.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MovieDbLight : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MovieDbLight)
            modules(dataModule, uiModule, networkModule)
        }
    }
}
