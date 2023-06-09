package com.sdevprem.mvvmtestingdemo.data.repository

import com.sdevprem.mvvmtestingdemo.data.api.ProductsAPI
import com.sdevprem.mvvmtestingdemo.data.model.ProductListItem
import com.sdevprem.mvvmtestingdemo.utils.NetworkResult
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class ProductRepositoryTest {

    @Mock
    lateinit var productApi : ProductsAPI

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetProducts_expectedEmptyResult() = runTest{
        Mockito
            .`when`(productApi.getProducts())
            .thenReturn(
                Response.success(emptyList())
            )

        val sut = ProductRepository(productApi)
        val result = sut.getProducts()

        assertEquals(true, result is NetworkResult.Success)
        assertEquals(0,result.data!!.size)
    }

    @Test
    fun testGetProducts_expectedProductList() = runTest {
        val productList = listOf(
            ProductListItem("","",1,"",40.3,"Prod 1"),
            ProductListItem("","",1,"",63.6,"Prod 2"),
        )
        Mockito
            .`when`(productApi.getProducts())
            .thenReturn(Response.success(productList))

        val sut = ProductRepository(productApi)
        val result = sut.getProducts()

        assertEquals(true,result is NetworkResult.Success)
        assertEquals(2, result.data!!.size)
        assertEquals("Prod 1",result.data!![0].title)
    }

    @Test
    fun testGetProducts_expectedError() = runTest{
        Mockito
            .`when`(productApi.getProducts())
            .thenReturn(
                Response.error(401,"Unauthorized".toResponseBody())
            )

        val sut = ProductRepository(productApi)
        val result = sut.getProducts()

        assertEquals(true, result is NetworkResult.Error)
        assertEquals("Something went wrong",result.message)
    }


    @After
    fun tearDown() {
    }
}