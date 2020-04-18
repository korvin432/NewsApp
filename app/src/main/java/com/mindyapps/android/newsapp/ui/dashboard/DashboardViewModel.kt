package com.mindyapps.android.newsapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mindyapps.android.newsapp.data.repository.NewsRepository
import com.mindyapps.android.newsapp.internal.lazyDeferred

class DashboardViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val topHeadlines by lazyDeferred{
        newsRepository.getTopHeadlines()
    }
}