package com.mindyapps.android.newsapp.data.model


import com.google.gson.annotations.SerializedName

data class TopHeadlinesResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)