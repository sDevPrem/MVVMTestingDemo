package com.sdevprem.mvvmtestingdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdevprem.mvvmtestingdemo.R
import com.sdevprem.mvvmtestingdemo.StoreApplication
import com.sdevprem.mvvmtestingdemo.ui.adapter.ProductAdapter
import com.sdevprem.mvvmtestingdemo.ui.viewmodel.MainViewModel
import com.sdevprem.mvvmtestingdemo.ui.viewmodel.MainViewModelFactory
import com.sdevprem.mvvmtestingdemo.utils.NetworkResult

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.productList)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val repository = (application as StoreApplication).productRepository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        mainViewModel.getProducts()

        mainViewModel.products.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    Log.d("CHEEZ", it.data.toString())
                    adapter = ProductAdapter(it.data!!)
                    recyclerView.adapter = adapter

                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()}
                else -> {}
            }
        }
    }
}