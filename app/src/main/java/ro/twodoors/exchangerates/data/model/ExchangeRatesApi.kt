package ro.twodoors.exchangerates.data.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ro.twodoors.exchangerates.data.model.ApiResponse

interface ExchangeRatesApi {

    @GET("/latest")
    suspend fun getRates(
        @Query("base") base : String
    ) : Response<ApiResponse>
}