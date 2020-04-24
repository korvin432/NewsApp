package com.mindyapps.android.newsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mindyapps.android.newsapp.data.model.Article

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(article: Article?)

    @Delete
    fun delete(article: Article?)

    @Query("SELECT * FROM articles")
    fun getArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: Int?):LiveData<Article>
}