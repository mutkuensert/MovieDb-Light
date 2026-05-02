package utils

import utils.stringresource.StringResource
import utils.stringresource.StringResourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
    single<StringResource> { StringResourceImpl(androidContext()) }
}