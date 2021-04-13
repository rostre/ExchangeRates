package ro.twodoors.exchangerates.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.twodoors.exchangerates.data.api.AuthInterceptor
import ro.twodoors.exchangerates.data.api.ExchangeRatesApi
import ro.twodoors.exchangerates.data.repository.Repository
import ro.twodoors.exchangerates.data.repository.RepositoryImpl
import ro.twodoors.exchangerates.util.DispatcherProvider

//private const val BASE_URL = "http://api.exchangeratesapi.io/"
private const val BASE_URL = "https://api.ratesapi.io/api/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideExchangeApi(okHttpClient : OkHttpClient) : ExchangeRatesApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(ExchangeRatesApi::class.java)

    @Provides
    fun provideRepository(api : ExchangeRatesApi) : Repository = RepositoryImpl(api)

    @Provides
    fun provideOkHttpClient() : OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor { message -> println("Logger: $message") }
                    .apply { level = HttpLoggingInterceptor.Level.BODY })
            //.addInterceptor(AuthInterceptor)
            .build()

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