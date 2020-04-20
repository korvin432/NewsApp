package com.mindyapps.android.newsapp.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mindyapps.android.newsapp.data.model.TopHeadlinesResponse
import com.mindyapps.android.newsapp.data.repository.NewsRepository
import com.mindyapps.android.newsapp.internal.lazyDeferred

class DashboardViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

     var country: String = "ru"
     var category: String = "sports"

    suspend fun getNewsSource(): LiveData<TopHeadlinesResponse> {
        return newsRepository.getTopHeadlines(category, country)
    }

}