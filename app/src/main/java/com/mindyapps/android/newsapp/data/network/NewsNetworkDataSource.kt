package com.mindyapps.android.newsapp.data.network

import androidx.lifecycle.LiveData
import com.mindyapps.android.newsapp.data.model.NewsResponse

interface NewsNetworkDataSource {
    val downloadedNews: LiveData<NewsResponse>
    val downloadedEverything: LiveData<NewsResponse>

    suspend fun fetchTopHeadlines(category:String, country:String)
    suspend fun fetchEverything(query:String)
}