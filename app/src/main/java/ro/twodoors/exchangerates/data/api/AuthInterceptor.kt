package ro.twodoors.exchangerates.data.api

import okhttp3.Interceptor
import okhttp3.Response
import ro.twodoors.exchangerates.BuildConfig

object AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("access_key", BuildConfig.API_KEY)
            .build()

        val newRequest = request.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}