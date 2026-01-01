package libraries

import libraries.stringresource.StrResource
import libraries.stringresource.StrResourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val librariesModule = module {
    single<StrResource> { StrResourceImpl(androidContext()) }
    single { AppScope() }
}