package com.mindyapps.android.newsapp.ui.article

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindyapps.android.newsapp.data.db.NewsDatabase
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.model.NewsResponse
import com.mindyapps.android.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val newsRepository: NewsRepository,
    application: Application
) : ViewModel() {

    private val newsDao = NewsDatabase.getDatabase(application).newsDao()

    fun insert(article: Article?) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.insertArticle(article, newsDao)
    }

    fun delete(article: Article?) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.deleteArticle(article, newsDao)
    }

    fun getArticle(id: Int?): LiveData<Article> {
        return newsRepository.getArticleById(newsDao, id)
    }

    fun getLastArticle(): LiveData<Article> {
        return newsRepository.getLastArticle(newsDao)
    }

}
