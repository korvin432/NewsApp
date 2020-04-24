package com.mindyapps.android.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mindyapps.android.newsapp.data.model.NewsResponse
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class SearchViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    suspend fun getSearch(query: String): LiveData<NewsResponse> {
        return newsRepository.getEverything(query = query)
    }
}