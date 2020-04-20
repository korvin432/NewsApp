package com.mindyapps.android.newsapp.ui.dashboard

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.model.TopHeadlinesResponse
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
    private lateinit var observerNewsArticle: Observer<TopHeadlinesResponse>

    private val sourceList = ArrayList<Article>()


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
        categoryChips = root.findViewById(R.id.category_chips)
        countryChips = root.findViewById(R.id.country_chips)
        searchButton = root.findViewById(R.id.search_button)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindChipGroup(categoryChips, countryChips)
        bindRecyclerView()

        searchButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            bindUI()
        }

        dashboardViewModel =
            ViewModelProvider(this, DashboardViewModelFactory(repositoryImpl)).get(
                DashboardViewModel::class.java
            )

        observerNewsArticle = Observer { newsSource ->
            if (newsSource?.articles != null && newsSource.articles.isNotEmpty()) {
                newsRecyclerAdapter.setArticles(newsSource.articles)
            }
        }
        launch(Main) {
            dashboardViewModel.getNewsSource()
                .observe(viewLifecycleOwner, observerNewsArticle)
        }

        setToolbar()
        bindUI()
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
        newsRecyclerAdapter = NewsRecyclerAdapter(sourceList.toMutableList())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = newsRecyclerAdapter
    }

    private fun bindUI() = launch(Main) {
        progressBar.visibility = View.GONE
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
