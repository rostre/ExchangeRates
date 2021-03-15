package ro.twodoors.exchangerates.data.repository

import ro.twodoors.exchangerates.data.model.ApiResponse
import ro.twodoors.exchangerates.util.Resource

interface Repository {

    suspend fun getRates(base: String) : Resource<ApiResponse>
}