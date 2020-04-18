package com.mindyapps.android.newsapp.data.network

import androidx.lifecycle.LiveData
import com.mindyapps.android.newsapp.data.model.TopHeadlinesResponse

interface NewsNetworkDataSource {
    val downloadedTopHeadlines: LiveData<TopHeadlinesResponse>

    suspend fun fetchTopHeadlines(category:String, country:String)
}