package core.libraries

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val librariesModule = module {
    single { StrResources(androidContext()) }
}