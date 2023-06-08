package com.sdevprem.mvvmtestingdemo.data.repository

import com.sdevprem.mvvmtestingdemo.data.api.ProductsAPI
import com.sdevprem.mvvmtestingdemo.data.model.ProductListItem
import com.sdevprem.mvvmtestingdemo.utils.NetworkResult

class ProductRepository(private val productsAPI: ProductsAPI) {

    suspend fun getProducts(): NetworkResult<List<ProductListItem>> {
        return try {
            val response = productsAPI.getProducts()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    NetworkResult.Success(responseBody)
                } else {
                    NetworkResult.Error("Something went wrong")
                }
            } else {
                NetworkResult.Error("Something went wrong")
            }
        }catch (e : Exception){
            NetworkResult.Error("Something went wrong")
        }
    }
}