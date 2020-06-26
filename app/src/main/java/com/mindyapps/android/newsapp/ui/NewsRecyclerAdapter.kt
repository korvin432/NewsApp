package com.mindyapps.android.newsapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.databinding.NewsItemBinding
import com.mindyapps.android.newsapp.internal.GlideApp
import kotlinx.android.synthetic.main.news_item.view.*

class NewsRecyclerAdapter(
    private var articles: MutableList<Article>
) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsHolder>() {

    var onItemClick: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): NewsHolder {
        val itemView: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.news_item, viewGroup, false)
        val binding = NewsItemBinding.inflate(LayoutInflater.from(viewGroup.context))
        return NewsHolder(itemView, binding)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val article: Article = articles[position]
        holder.binding.article = article
        holder.binding.executePendingBindings()
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

    inner class NewsHolder(private val view: View, val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(articles[adapterPosition])
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?){
    if (!url.isNullOrEmpty()) {
        GlideApp.with(view.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(view)
    }
}