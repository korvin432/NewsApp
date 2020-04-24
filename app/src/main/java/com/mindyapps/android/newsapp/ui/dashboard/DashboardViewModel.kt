package com.mindyapps.android.newsapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mindyapps.android.newsapp.data.model.NewsResponse
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class DashboardViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

     var country: String = "us"
     var category: String = "general"

    suspend fun getNewsSource(): LiveData<NewsResponse> {
        return newsRepository.getTopHeadlines(category, country)
    }

}