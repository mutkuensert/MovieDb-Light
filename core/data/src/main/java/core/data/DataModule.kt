package core.data

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import core.data.account.AccountRepositoryImpl
import core.data.account.AccountService
import core.data.auth.AuthenticationRepositoryImpl
import core.data.auth.AuthenticationService
import core.data.auth.LogoutTrigger
import core.data.network.Configs
import core.data.network.ResultCallAdapterFactory
import core.data.network.interceptor.AccountIdInterceptor
import core.data.network.interceptor.ApiKeyInterceptor
import core.data.network.interceptor.LanguageInterceptor
import core.database.LanguagePreference
import core.database.user.UserManager
import core.domain.ApiKeyManager
import core.domain.account.AccountRepository
import core.domain.auth.AuthStateProvider
import core.domain.auth.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import utils.Constants
import utils.stringresource.StringResource
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindApiKeyManager(apiKeyManager: ApiKeyManagerImpl): ApiKeyManager

    @Binds
    @Singleton
    abstract fun bindAuthStateProvider(sessionManager: SessionManager): AuthStateProvider

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        authenticationRepository: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(accountRepository: AccountRepositoryImpl): AccountRepository

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json = getJson()

        @Provides
        @Singleton
        fun provideOkHttpClient(
            @ApplicationContext context: Context,
            userManager: UserManager,
            logoutTrigger: LogoutTrigger,
            apiKeyManager: ApiKeyManager,
            stringResource: StringResource,
            remoteConfig: RemoteConfig,
            languagePreference: LanguagePreference,
            json: Json,
        ): OkHttpClient {
            return OkHttpClient()
                .newBuilder()
                .addInterceptor(LanguageInterceptor(languagePreference))
                .addInterceptor(
                    ApiKeyInterceptor(
                        apiKeyManager,
                        getJson(),
                        stringResource,
                        remoteConfig
                    )
                )
                .addInterceptor(
                    AccountIdInterceptor(
                        userManager,
                        logoutTrigger,
                        json,
                        stringResource
                    )
                )
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(ChuckerInterceptor(context))
                .callTimeout(Constants.TIMEOUT_SEC, TimeUnit.SECONDS)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            json: Json,
            client: OkHttpClient,
            stringResource: StringResource,
        ): Retrofit {
            return Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(ResultCallAdapterFactory(json, stringResource))
                .baseUrl(Configs.BASE_URL)
                .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
                .build()
        }

        @Provides
        fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
            return retrofit.create(AuthenticationService::class.java)
        }

        @Provides
        fun provideAccountService(retrofit: Retrofit): AccountService {
            return retrofit.create(AccountService::class.java)
        }

        @Provides
        @Singleton
        fun provideRemoteConfig(): RemoteConfig {
            return RemoteConfig()
        }
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
