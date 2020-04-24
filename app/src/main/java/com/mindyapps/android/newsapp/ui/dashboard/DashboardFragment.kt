package com.mindyapps.android.newsapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.model.NewsResponse
import com.mindyapps.android.newsapp.data.network.ConnectivityInterceptorImpl
import com.mindyapps.android.newsapp.data.network.NewsApi
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSourceImpl
import com.mindyapps.android.newsapp.data.repository.NewsRepositoryImpl
import com.mindyapps.android.newsapp.ui.NewsRecyclerAdapter
import com.mindyapps.android.newsapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class DashboardFragment : ScopedFragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var repositoryImpl: NewsRepositoryImpl
    private lateinit var dataSourceImpl: NewsNetworkDataSourceImpl
    private lateinit var api: NewsApi
    private lateinit var conn: ConnectivityInterceptorImpl
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var categoryChips: ChipGroup
    private lateinit var countryChips: ChipGroup
    private lateinit var searchButton: MaterialButton
    private lateinit var observerNewsArticle: Observer<NewsResponse>

    private val sourceList = ArrayList<Article>()
    private var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_dashboard, container, false)
            conn = ConnectivityInterceptorImpl(activity!!.applicationContext)
            api = NewsApi(conn)
            dataSourceImpl = NewsNetworkDataSourceImpl(api)
            repositoryImpl = NewsRepositoryImpl(dataSourceImpl)
            recyclerView = root!!.findViewById(R.id.dashboard_recycler)
            progressBar = root!!.findViewById(R.id.progress_circular)
            categoryChips = root!!.findViewById(R.id.category_chips)
            countryChips = root!!.findViewById(R.id.country_chips)
            searchButton = root!!.findViewById(R.id.search_button)

            dashboardViewModel =
                ViewModelProvider(this, DashboardViewModelFactory(repositoryImpl)).get(
                    DashboardViewModel::class.java
                )
            bindChipGroup(categoryChips, countryChips)
            bindRecyclerView()
        }

        return root
    }

    private fun loadNews() {
        lifecycleScope.launch {
            dashboardViewModel.getNewsSource().observe(viewLifecycleOwner, observerNewsArticle)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchButton.setOnClickListener {
            newsRecyclerAdapter.clearArticles()
            progressBar.visibility = View.VISIBLE
            loadNews()
            bindUI()
        }

        observerNewsArticle = Observer { newsSource ->
            if (newsSource?.articles != null && newsSource.articles.isNotEmpty()) {
                progressBar.visibility = View.GONE
                newsRecyclerAdapter.setArticles(newsSource.articles)
            }
        }

        setToolbar()
    }

    private fun setToolbar() {
        var isShow = true
        var scrollRange = -1
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar.title = "Dashboard"
                isShow = true
            } else if (isShow) {
                collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

    private fun bindChipGroup(categoryChips: ViewGroup, countryChips: ViewGroup) {
        val categories = resources.getStringArray(R.array.categories).toList()
        val countries = resources.getStringArray(R.array.countries).toList()
        for (category in categories) {
            val chip = layoutInflater.inflate(R.layout.chip_layout, categoryChips, false) as Chip
            chip.text = category
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            categoryChips.addView(chip)
        }
        for (country in countries) {
            val chip = layoutInflater.inflate(R.layout.chip_layout, countryChips, false) as Chip
            chip.text = country
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            countryChips.addView(chip)
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
                .navigate(R.id.action_navigation_dashboard_to_navigation_article, bundle)
        }
    }

    private fun bindUI() = launch(Main) {
        try {
            val countryChip: Chip = countryChips.findViewById(countryChips.checkedChipId)
            val categoryChip: Chip = categoryChips.findViewById(categoryChips.checkedChipId)
            dashboardViewModel.country = countryChip.text.toString()
            dashboardViewModel.category = categoryChip.text.toString()
            dashboardViewModel.getNewsSource().observe(viewLifecycleOwner, observerNewsArticle)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
