package com.mindyapps.android.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mindyapps.android.newsapp.data.repository.NewsRepository
import com.mindyapps.android.newsapp.ui.dashboard.DashboardViewModel

class SearchViewModelFactory(
    private val newsRepository: NewsRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(newsRepository) as T
    }
}