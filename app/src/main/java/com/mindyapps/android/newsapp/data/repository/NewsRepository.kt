package com.mindyapps.android.newsapp.data.repository

import androidx.lifecycle.LiveData
import com.mindyapps.android.newsapp.data.db.NewsDao
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.model.NewsResponse

interface NewsRepository {
    suspend fun getTopHeadlines(category: String, country: String): LiveData<NewsResponse>
    suspend fun getEverything(query: String): LiveData<NewsResponse>
    suspend fun insertArticle(article: Article?, newsDao: NewsDao)
    fun deleteArticle(article: Article?, newsDao: NewsDao)
    fun deleteLastArticle(newsDao: NewsDao)
    fun getFavouriteArticles(newsDao: NewsDao): LiveData<List<Article>>
    fun getArticleById(newsDao: NewsDao, id: Int?): LiveData<Article>
    fun getLastArticle(newsDao: NewsDao): LiveData<Article>
}