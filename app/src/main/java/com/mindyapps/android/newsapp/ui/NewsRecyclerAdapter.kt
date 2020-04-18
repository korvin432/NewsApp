package com.mindyapps.android.newsapp.ui

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import kotlinx.android.synthetic.main.news_item.view.*

class NewsRecyclerAdapter(
    private var articles: List<Article>
) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsHolder>()  {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): NewsHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.news_item, viewGroup, false)
        return NewsHolder(itemView)
    }

    override fun getItemCount() =  articles.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val article: Article = articles[position]
        setPropertiesForArticleViewHolder(holder, article)
    }

    fun setArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    private fun setPropertiesForArticleViewHolder(articleViewHolder: NewsHolder, article: Article) {
            articleViewHolder.author.text = article.title
            articleViewHolder.description.text = article.description
    }

    inner class NewsHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView by lazy { view.textView_author }
        val description: TextView by lazy { view.textView_description }
    }
}