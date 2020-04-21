package com.mindyapps.android.newsapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.mindyapps.android.newsapp.data.model.TopHeadlinesResponse
import com.mindyapps.android.newsapp.data.network.NewsApi
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSource
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepositoryImpl(
    private val dataSource: NewsNetworkDataSourceImpl
) : NewsRepository {

    override suspend fun getTopHeadlines(category:String, country:String): LiveData<TopHeadlinesResponse> {
        return withContext(Dispatchers.IO) {
            dataSource.fetchTopHeadlines(category, country)
            return@withContext dataSource.downloadedTopHeadlines
        }
    }
}