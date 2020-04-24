package com.mindyapps.android.newsapp.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.model.NewsResponse
import com.mindyapps.android.newsapp.data.network.ConnectivityInterceptorImpl
import com.mindyapps.android.newsapp.data.network.NewsApi
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSourceImpl
import com.mindyapps.android.newsapp.data.repository.NewsRepositoryImpl
import com.mindyapps.android.newsapp.ui.NewsRecyclerAdapter
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {
    private lateinit var repositoryImpl: NewsRepositoryImpl
    private lateinit var dataSourceImpl: NewsNetworkDataSourceImpl
    private lateinit var api: NewsApi
    private lateinit var conn: ConnectivityInterceptorImpl
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter
    private lateinit var observerNewsArticle: Observer<List<Article>>

    private val sourceList = ArrayList<Article>()
    private lateinit var favouritesViewModel: FavouritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        conn = ConnectivityInterceptorImpl(activity!!.applicationContext)
        api = NewsApi(conn)
        dataSourceImpl = NewsNetworkDataSourceImpl(api)
        repositoryImpl = NewsRepositoryImpl(dataSourceImpl)
        recyclerView = root.findViewById(R.id.favourites_recycler)

        favouritesViewModel =
            ViewModelProvider(
                this,
                FavouritesViewModelFactory(repositoryImpl, activity!!.application)
            ).get(FavouritesViewModel::class.java)
        bindRecyclerView()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observerNewsArticle = Observer { newsSource ->
            if (newsSource.isNotEmpty()) {
                newsRecyclerAdapter.setArticles(newsSource)
            }
        }
        loadNews()
    }

    private fun loadNews() {
        lifecycleScope.launch {
            favouritesViewModel.allArticles.observe(viewLifecycleOwner, observerNewsArticle)
        }
    }

    private fun bindRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity!!.applicationContext)
        newsRecyclerAdapter =
            NewsRecyclerAdapter(sourceList.toMutableList(), activity!!.applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = newsRecyclerAdapter
        newsRecyclerAdapter.onItemClick = { article ->
            val bundle = bundleOf("article" to article)
            view!!.findNavController()
                .navigate(R.id.action_navigation_favourites_to_navigation_article, bundle)
        }
    }
}
