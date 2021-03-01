package ro.twodoors.exchangerates.main

import ro.twodoors.exchangerates.data.model.ApiResponse
import ro.twodoors.exchangerates.util.Resource

interface MainRepository {

    suspend fun getRates(base: String) : Resource<ApiResponse>
}