package com.mindyapps.android.newsapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class DashboardViewModelFactory(
    private val newsRepository: NewsRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(newsRepository) as T
    }
}