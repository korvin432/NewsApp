package com.mindyapps.android.newsapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mindyapps.android.newsapp.internal.Constants
import com.mindyapps.android.newsapp.ui.NewsRecyclerAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class SearchFragment : Fragment(), View.OnFocusChangeListener, SearchView.OnQueryTextListener,
    KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: SearchViewModelFactory by instance()
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchView: SearchView
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
            searchView = root!!.findViewById(R.id.search_view)
            recyclerView = root!!.findViewById(R.id.search_recycler)
            progressBar = root!!.findViewById(R.id.search_progress)

            searchViewModel =
                ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
            bindRecyclerView()
        }
        return root
    }

    private fun bindRecyclerView() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        newsRecyclerAdapter = NewsRecyclerAdapter(sourceList.toMutableList(), requireContext())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = newsRecyclerAdapter
        newsRecyclerAdapter.onItemClick = { article ->
            val bundle = bundleOf(Constants.KEY_ARTICLE to article)
            requireView().findNavController()
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
