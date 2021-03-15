package ro.twodoors.exchangerates.data.repository

import ro.twodoors.exchangerates.data.api.ExchangeRatesApi
import ro.twodoors.exchangerates.data.model.ApiResponse
import ro.twodoors.exchangerates.data.repository.Repository
import ro.twodoors.exchangerates.util.Resource
import java.lang.Exception
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ExchangeRatesApi
) : Repository {

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