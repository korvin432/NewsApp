package com.mindyapps.android.newsapp.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mindyapps.android.newsapp.internal.Constants
import com.mindyapps.android.newsapp.ui.NewsRecyclerAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FavouritesFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: FavouritesViewModelFactory by instance()
    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter
    private lateinit var observerNewsArticle: Observer<List<Article>>

    private val sourceList = ArrayList<Article>()
    private var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_favourites, container, false)
            recyclerView = root!!.findViewById(R.id.favourites_recycler)

            favouritesViewModel = ViewModelProvider(this, viewModelFactory)
                .get(FavouritesViewModel::class.java)
            bindRecyclerView()
        }
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
        linearLayoutManager = LinearLayoutManager(requireContext())
        newsRecyclerAdapter = NewsRecyclerAdapter(sourceList.toMutableList())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = newsRecyclerAdapter
        newsRecyclerAdapter.onItemClick = { article ->
            val bundle = bundleOf(Constants.KEY_ARTICLE to article)
            requireView().findNavController()
                .navigate(R.id.action_navigation_favourites_to_navigation_article, bundle)
        }
    }
}
