package uz.napa.eyecaria.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import uz.napa.eyecaria.network.Api
import uz.napa.eyecaria.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideRepository(api: Api): Repository {
        return Repository(api)
    }
}