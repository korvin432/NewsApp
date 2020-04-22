package com.mindyapps.android.newsapp.ui.article

import androidx.lifecycle.ViewModel
import com.mindyapps.android.newsapp.data.repository.NewsRepository

class ArticleViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

}
