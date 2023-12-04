package com.example.beritakini

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.beritakini.api.ApiClient
import com.example.beritakini.api.adapter.NewsAdapter
import com.example.beritakini.api.model.Article
import com.example.beritakini.api.model.NewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var call: Call<NewResponse>
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var searchView: SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycle_view)
        searchView = findViewById(R.id.searchView)

        newsAdapter = NewsAdapter { article ->  articleOnClick(article)}
        recyclerView.adapter = newsAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false )
        swipeRefreshLayout.setOnRefreshListener {
            getData()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (::newsAdapter.isInitialized) {
                    newsAdapter.filter.filter(newText)
                }
                return false
            }
        })

        getData()
    }

    private fun articleOnClick(article: Article){
        Toast.makeText(applicationContext, article.description, Toast.LENGTH_SHORT).show()
    }

    private fun getData(){
        swipeRefreshLayout.isRefreshing = true

        call = ApiClient.newsServices.getAll()
        call.enqueue(object : Callback<NewResponse>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<NewResponse>, response: Response<NewResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful){
                    newsAdapter.originalList = response.body()?.articles ?: emptyList()
                    newsAdapter.submitList(newsAdapter.originalList)
                    newsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}