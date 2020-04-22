package com.mindyapps.android.newsapp.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class ArticleViewModelFactory(
    private val newsRepository: NewsRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleViewModel(newsRepository) as T
    }
}