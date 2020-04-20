package com.mindyapps.android.newsapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mindyapps.android.newsapp.data.model.TopHeadlinesResponse
import java.lang.Exception

class NewsNetworkDataSourceImpl(
    private val apiService: NewsApi
) : NewsNetworkDataSource {

    private val _downloadedTopHeadlines = MutableLiveData<TopHeadlinesResponse>()
    override val downloadedTopHeadlines: LiveData<TopHeadlinesResponse>
        get() = _downloadedTopHeadlines

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
}