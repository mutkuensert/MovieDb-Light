package moviedblight

import android.app.Application
import moviedblight.core.injection.modules.core.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MovieDbLight : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MovieDbLight)
            modules(dataModule)
        }
    }
}
