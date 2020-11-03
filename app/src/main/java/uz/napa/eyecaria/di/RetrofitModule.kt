package uz.napa.eyecaria.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import uz.napa.eyecaria.network.Api
import uz.napa.eyecaria.network.BASE_URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofitClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
    }

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit.Builder): Api {
        return retrofit.build().create(Api::class.java)
    }
}