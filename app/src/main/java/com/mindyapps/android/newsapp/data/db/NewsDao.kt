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

    @Query("DELETE FROM articles WHERE id = (SELECT MAX(id) FROM articles)")
    fun deleteLastArticle()

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: Int?):LiveData<Article>

    @Query("SELECT * FROM articles ORDER BY id DESC LIMIT 1")
    fun getLastArticle():LiveData<Article>
}