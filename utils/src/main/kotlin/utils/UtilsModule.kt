package utils

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import utils.stringresource.StringResource
import utils.stringresource.StringResourceImpl

val utilsModule = module {
    single<StringResource> { StringResourceImpl(androidContext()) }
}