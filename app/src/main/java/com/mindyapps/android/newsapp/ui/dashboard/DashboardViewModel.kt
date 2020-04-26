package com.mindyapps.android.newsapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mindyapps.android.newsapp.data.model.NewsResponse
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class DashboardViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    suspend fun getNewsSource(category: String, country: String): LiveData<NewsResponse> {
        return newsRepository.getTopHeadlines(category, country)
    }

}