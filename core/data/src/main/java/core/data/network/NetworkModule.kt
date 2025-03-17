package core.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import core.data.StrResources
import core.data.network.interceptor.AccountIdInterceptor
import core.data.network.interceptor.ApiKeyInterceptor
import core.database.user.UserManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single { getJson() }
    single { UserManager(androidContext(), get()) }
    single {
        Retrofit.Builder()
            .client(getClient(get(), get()))
            .addCallAdapterFactory(ResultCallAdapterFactory(get<Json>(), get<StrResources>()))
            .baseUrl(Configs.BASE_URL)
            .addConverterFactory(get<Json>().asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }
    single { StrResources(androidContext()) }
}

private fun getJson(): Json {
    return Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
    }
}

private fun getClient(context: Context, userManager: UserManager): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(ApiKeyInterceptor())
        .addInterceptor(AccountIdInterceptor(userManager))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .addInterceptor(ChuckerInterceptor(context))
        .build()
}