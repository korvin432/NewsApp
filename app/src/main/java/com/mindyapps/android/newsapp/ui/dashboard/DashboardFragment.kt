package com.mindyapps.android.newsapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.network.ConnectivityInterceptorImpl
import com.mindyapps.android.newsapp.data.network.NewsApi
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSourceImpl
import com.mindyapps.android.newsapp.data.repository.NewsRepositoryImpl
import com.mindyapps.android.newsapp.ui.NewsRecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var repositoryImpl: NewsRepositoryImpl
    private lateinit var dataSourceImpl: NewsNetworkDataSourceImpl
    private lateinit var api: NewsApi
    private lateinit var conn: ConnectivityInterceptorImpl
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        conn = ConnectivityInterceptorImpl(activity!!.applicationContext)
        api = NewsApi(conn)
        dataSourceImpl = NewsNetworkDataSourceImpl(api)
        repositoryImpl = NewsRepositoryImpl(dataSourceImpl)
        recyclerView = root.findViewById(R.id.dashboard_recycler)
        progressBar = root.findViewById(R.id.progress_circular)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dashboardViewModel =
            ViewModelProvider(this, DashboardViewModelFactory(repositoryImpl)).get(
                DashboardViewModel::class.java
            )
        progressBar.visibility = View.VISIBLE
        bindUI()
    }


    private fun bindRecyclerView(articles: List<Article>) {
        linearLayoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        newsRecyclerAdapter = NewsRecyclerAdapter(articles)
        recyclerView.adapter = newsRecyclerAdapter
        progressBar.visibility = View.GONE
    }

    private fun bindUI() = MainScope().launch(Dispatchers.IO) {
        val topHeadlines = dashboardViewModel.topHeadlines.await()
        withContext(Main) {
            topHeadlines.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer
                bindRecyclerView(it.articles)
            })
        }
    }
}
