package ro.twodoors.exchangerates.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.twodoors.exchangerates.data.api.ExchangeRatesApi
import ro.twodoors.exchangerates.data.repository.Repository
import ro.twodoors.exchangerates.data.repository.RepositoryImpl
import ro.twodoors.exchangerates.util.DispatcherProvider

private const val BASE_URL = "https://api.exchangeratesapi.io/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideExchangeApi() : ExchangeRatesApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ExchangeRatesApi::class.java)

    @Provides
    fun provideRepository(api : ExchangeRatesApi) : Repository = RepositoryImpl(api)

    @Provides
    fun provideDispatchers() : DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }

}