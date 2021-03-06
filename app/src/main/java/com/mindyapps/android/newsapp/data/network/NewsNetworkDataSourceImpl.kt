package com.mindyapps.android.newsapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mindyapps.android.newsapp.data.model.NewsResponse
import java.lang.Exception

class NewsNetworkDataSourceImpl(
    private val apiService: NewsApi
) : NewsNetworkDataSource {

    private val _downloadedTopHeadlines = MutableLiveData<NewsResponse>()
    override val downloadedNews: LiveData<NewsResponse>
        get() = _downloadedTopHeadlines

    private val _downloadedEverything = MutableLiveData<NewsResponse>()
    override val downloadedEverything: LiveData<NewsResponse>
        get() = _downloadedEverything

    override suspend fun fetchTopHeadlines(category:String, country:String) {
        try {
            val fetchedTopHeadlines = apiService
                .getTopHeadlinesAsync(country,category)
                .await()
            _downloadedTopHeadlines.postValue(fetchedTopHeadlines)
        }
        catch (e: Exception){
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    override suspend fun fetchEverything(query: String) {
        try {
            val fetchedEverything = apiService
                .getEverythingAsync(query)
                .await()
            _downloadedEverything.postValue(fetchedEverything)
        }
        catch (e: Exception){
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}