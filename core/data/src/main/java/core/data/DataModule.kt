package core.data

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import core.data.account.AccountRepositoryImpl
import core.data.account.AccountService
import core.data.auth.AuthenticationRepositoryImpl
import core.data.auth.AuthenticationService
import core.data.network.Configs
import core.data.network.ResultCallAdapterFactory
import core.data.network.interceptor.AccountIdInterceptor
import core.data.network.interceptor.ApiKeyInterceptor
import core.database.user.UserManager
import core.domain.AccountRepository
import core.domain.AuthState
import core.domain.AuthenticationRepository
import core.libraries.StrResources
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val dataModule = module {
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
    single { get<Retrofit>().create(AuthenticationService::class.java) }
    single { get<Retrofit>().create(AccountService::class.java) }
    single { SessionManager(androidContext()) }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get(), get(), get())
    }
    single<AccountRepository>(createdAtStart = true) {
        AccountRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single<AuthState> { AuthStateImpl(get()) }
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
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(ChuckerInterceptor(context))
        .build()
}