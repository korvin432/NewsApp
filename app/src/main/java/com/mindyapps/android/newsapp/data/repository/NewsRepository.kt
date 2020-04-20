package com.mindyapps.android.newsapp.data.repository

import androidx.lifecycle.LiveData
import com.mindyapps.android.newsapp.data.model.TopHeadlinesResponse

interface NewsRepository {
    suspend fun getTopHeadlines(category:String, country:String): LiveData<TopHeadlinesResponse>
}