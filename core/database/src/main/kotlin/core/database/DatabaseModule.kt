package core.database

import android.content.Context
import core.database.encryptedpreferences.EncryptedPreferences
import core.domain.ProfileFeatureAvailability
import core.domain.common.LanguagePreferenceUpdateState
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindLanguagePreferenceUpdateState(
        languagePreference: LanguagePreference
    ): LanguagePreferenceUpdateState

    @Binds
    @Singleton
    abstract fun bindEncryptedPreferences(encryptedPreferences: EncryptedPreferences): ProfileFeatureAvailability

    companion object {
        @Provides
        @Singleton
        fun provideLanguagePreference(@ApplicationContext context: Context): LanguagePreference {
            return LanguagePreference(context)
        }
    }
}
