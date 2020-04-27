package com.mindyapps.android.newsapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.internal.GlideApp
import kotlinx.android.synthetic.main.news_item.view.*

class NewsRecyclerAdapter(
    private var articles: MutableList<Article>,
    private var context: Context
) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsHolder>() {

    var onItemClick: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): NewsHolder {
        val itemView: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.news_item, viewGroup, false)
        return NewsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val article: Article = articles[position]
        setPropertiesForArticleViewHolder(holder, article)
    }

    fun clearArticles() {
        articles.clear()
        notifyDataSetChanged()
    }

    fun setArticles(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }


    private fun setPropertiesForArticleViewHolder(articleViewHolder: NewsHolder, article: Article) {
        articleViewHolder.author.text = article.title
        articleViewHolder.description.text = article.description
        GlideApp.with(context)
            .load(article.urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(articleViewHolder.image)
    }

    inner class NewsHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView by lazy { view.textView_author }
        val description: TextView by lazy { view.textView_description }
        val image: ImageView by lazy { view.article_image }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(articles[adapterPosition])
            }
        }
    }
}