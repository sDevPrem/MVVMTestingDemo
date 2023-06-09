package com.sdevprem.mvvmtestingdemo.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sdevprem.mvvmtestingdemo.data.repository.ProductRepository
import com.sdevprem.mvvmtestingdemo.getOrAwaitValue
import com.sdevprem.mvvmtestingdemo.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @Mock
    lateinit var repository: ProductRepository
    private val testDispatcher = StandardTestDispatcher()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_getProducts() = runTest{
        Mockito.`when`(repository.getProducts())
            .thenReturn(NetworkResult.Success(emptyList()))

        val sut = MainViewModel(repository)
        sut.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = sut.products.getOrAwaitValue()
        assertEquals(0,result.data!!.size)
    }
    @Test
    fun test_getProducts_expectedError() = runTest{
        Mockito.`when`(repository.getProducts())
            .thenReturn(NetworkResult.Error("Something went wrong"))

        val sut = MainViewModel(repository)
        sut.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = sut.products.getOrAwaitValue()
        assertEquals(true,result is NetworkResult.Error)
        assertEquals("Something went wrong",result.message)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}