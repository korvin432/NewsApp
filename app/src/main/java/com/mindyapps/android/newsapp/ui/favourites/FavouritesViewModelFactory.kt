package com.mindyapps.android.newsapp.ui.favourites

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class FavouritesViewModelFactory(
    private val newsRepository: NewsRepository,
    private val application: Application
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouritesViewModel(newsRepository, application) as T
    }
}