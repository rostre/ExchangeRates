package ro.twodoors.exchangerates.main

import ro.twodoors.exchangerates.data.ExchangeRatesApi
import ro.twodoors.exchangerates.data.model.ApiResponse
import ro.twodoors.exchangerates.util.Resource
import java.lang.Exception
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: ExchangeRatesApi
) : MainRepository {

    override suspend fun getRates(base: String): Resource<ApiResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null){
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }

        } catch (e : Exception){
            Resource.Error(e.message ?: "An error ocurred")
        }
    }
}