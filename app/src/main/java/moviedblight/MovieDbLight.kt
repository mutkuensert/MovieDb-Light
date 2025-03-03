package moviedblight

import android.app.Application
import core.data.network.networkModule
import core.injection.common.core.dataModule
import core.injection.common.core.uiModule
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
