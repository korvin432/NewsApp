package com.mindyapps.android.newsapp.ui.search

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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


class SearchFragment : Fragment(), View.OnFocusChangeListener, SearchView.OnQueryTextListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchView: SearchView
    private lateinit var repositoryImpl: NewsRepositoryImpl
    private lateinit var dataSourceImpl: NewsNetworkDataSourceImpl
    private lateinit var api: NewsApi
    private lateinit var conn: ConnectivityInterceptorImpl
    private lateinit var observerNewsArticle: Observer<NewsResponse>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val sourceList = ArrayList<Article>()
    private var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_search, container, false)
            conn = ConnectivityInterceptorImpl(activity!!.applicationContext)
            api = NewsApi(conn)
            dataSourceImpl = NewsNetworkDataSourceImpl(api)
            repositoryImpl = NewsRepositoryImpl(dataSourceImpl)
            searchView = root!!.findViewById(R.id.search_view)
            recyclerView = root!!.findViewById(R.id.search_recycler)
            progressBar = root!!.findViewById(R.id.search_progress)

            searchViewModel = ViewModelProvider(
                this, SearchViewModelFactory(repositoryImpl)
            ).get(
                SearchViewModel::class.java
            )
            bindRecyclerView()
        }
        return root
    }

    private fun bindRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity!!.applicationContext)
        newsRecyclerAdapter =
            NewsRecyclerAdapter(sourceList.toMutableList(), activity!!.applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = newsRecyclerAdapter
        newsRecyclerAdapter.onItemClick = { article ->
            val bundle = bundleOf("imageUrl" to article)
            view!!.findNavController()
                .navigate(R.id.action_navigation_search_to_navigation_article, bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView.setOnQueryTextListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observerNewsArticle = Observer { newsSource ->
            if (newsSource?.articles != null && newsSource.articles.isNotEmpty()) {
                progressBar.visibility = View.GONE
                newsRecyclerAdapter.setArticles(newsSource.articles)
            }
        }
    }

    private fun loadNews(query: String) {
        lifecycleScope.launch {
            searchViewModel.getSearch(query).observe(viewLifecycleOwner, observerNewsArticle)
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        newsRecyclerAdapter.clearArticles()
        progressBar.visibility = View.VISIBLE
        loadNews(query)
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false

    }

    override fun onFocusChange(p0: View?, p1: Boolean) {

    }


}
