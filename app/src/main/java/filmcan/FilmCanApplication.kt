package filmcan

import android.app.Application
import com.mutkuensert.filmcan.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FilmCanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimber()
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
