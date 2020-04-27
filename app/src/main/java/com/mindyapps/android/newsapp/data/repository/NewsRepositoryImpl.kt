package com.mindyapps.android.newsapp.data.repository

import androidx.lifecycle.LiveData
import com.mindyapps.android.newsapp.data.db.NewsDao
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.model.NewsResponse
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepositoryImpl(
    private val dataSource: NewsNetworkDataSource
) : NewsRepository {

    override suspend fun getTopHeadlines(category:String, country:String): LiveData<NewsResponse> {
        return withContext(Dispatchers.IO) {
            dataSource.fetchTopHeadlines(category, country)
            return@withContext dataSource.downloadedNews
        }
    }

    override suspend fun getEverything(
        query: String
    ): LiveData<NewsResponse> {
        return withContext(Dispatchers.IO) {
            dataSource.fetchEverything(query)
            return@withContext dataSource.downloadedEverything
        }
    }

    override suspend fun insertArticle(article: Article?, newsDao: NewsDao) {
        newsDao.insert(article)
    }

    override fun deleteArticle(article: Article?, newsDao: NewsDao) {
        newsDao.delete(article)
    }

    override fun deleteLastArticle(newsDao: NewsDao) {
        newsDao.deleteLastArticle()
    }

    override fun getFavouriteArticles(newsDao: NewsDao): LiveData<List<Article>> {
        return newsDao.getArticles()
    }

    override fun getArticleById(newsDao: NewsDao, id: Int?): LiveData<Article> {
        return newsDao.getArticleById(id)
    }

    override fun getLastArticle(newsDao: NewsDao): LiveData<Article> {
        return newsDao.getLastArticle()
    }
}