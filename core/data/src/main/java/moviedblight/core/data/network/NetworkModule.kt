package moviedblight.core.data.network

import kotlinx.serialization.json.Json
import moviedblight.core.data.network.interceptor.AccountIdInterceptor
import moviedblight.core.data.network.interceptor.ApiKeyInterceptor
import moviedblight.core.database.user.UserManager
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
            .client(getClient(get()))
            .baseUrl(Configs.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }
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

private fun getClient(userManager: UserManager): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(ApiKeyInterceptor())
        .addInterceptor(AccountIdInterceptor(userManager))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
}