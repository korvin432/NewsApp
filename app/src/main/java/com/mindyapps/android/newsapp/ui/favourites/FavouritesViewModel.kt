package com.mindyapps.android.newsapp.ui.favourites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mindyapps.android.newsapp.data.db.NewsDatabase
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class FavouritesViewModel(
    newsRepository: NewsRepository,
    application: Application
) : ViewModel() {

    val allArticles: LiveData<List<Article>>

    init {
        val newsDao = NewsDatabase.getDatabase(application).newsDao()
        allArticles = newsRepository.getFavouriteArticles(newsDao)
    }
}