package com.sdevprem.mvvmtestingdemo.data.api

import com.sdevprem.mvvmtestingdemo.data.model.ProductListItem
import retrofit2.Response
import retrofit2.http.GET

interface ProductsAPI {

    @GET("/products")
    suspend fun getProducts() : Response<List<ProductListItem>>

}