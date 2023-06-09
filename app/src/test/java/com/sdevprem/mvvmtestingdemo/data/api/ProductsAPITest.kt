package com.sdevprem.mvvmtestingdemo.data.api

import com.sdevprem.mvvmtestingdemo.Helper
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsAPITest {

    private lateinit var mockWebServer : MockWebServer
    private lateinit var productsAPI: ProductsAPI

    @Before
    fun setUp(){
        mockWebServer = MockWebServer()
        productsAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsAPI::class.java)
    }

    @Test
    fun testGetProducts() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)

        val response = productsAPI.getProducts()
        mockWebServer.takeRequest()

        assertEquals(true,response.body()!!.isEmpty())
    }

    @Test
    fun testGetProducts_expectedProductList() = runTest{
        val mockResponse = MockResponse()
        val content = Helper.readFileResources("/product_list_response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = productsAPI.getProducts()
        mockWebServer.takeRequest()

        assertEquals(false,response.body()!!.isEmpty())
        assertEquals(2,response.body()!!.size)
    }

    @Test
    fun testGetProducts_expectedError() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went wrong")
        mockWebServer.enqueue(mockResponse)

        val response = productsAPI.getProducts()
        mockWebServer.takeRequest()

        assertEquals(false,response.isSuccessful)
        assertEquals(404,response.code())
    }


    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}