package filmcan

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InjectionModule {

    @Binds
    @Singleton
    fun bindRemoteConfig(remoteConfigImpl: RemoteConfigImpl): RemoteConfig
}