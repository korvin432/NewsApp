package com.mindyapps.android.newsapp.data.repository

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

    override suspend fun getTopHeadlines(): LiveData<TopHeadlinesResponse> {
        return withContext(Dispatchers.IO) {
            dataSource.fetchTopHeadlines("technology", "ru")
            return@withContext dataSource.downloadedTopHeadlines
        }
    }
}