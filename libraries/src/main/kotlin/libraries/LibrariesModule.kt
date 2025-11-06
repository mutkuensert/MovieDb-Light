package libraries

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val librariesModule = module {
    single { StrResource(androidContext()) }
    single { AppScope() }
}