package utils

import utils.stringresource.StrResource
import utils.stringresource.StrResourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
    single<StrResource> { StrResourceImpl(androidContext()) }
}