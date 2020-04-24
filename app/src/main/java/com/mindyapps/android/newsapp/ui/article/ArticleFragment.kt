package com.mindyapps.android.newsapp.ui.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.AppBarLayout
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.data.network.ConnectivityInterceptorImpl
import com.mindyapps.android.newsapp.data.network.NewsApi
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSourceImpl
import com.mindyapps.android.newsapp.data.repository.NewsRepositoryImpl
import com.mindyapps.android.newsapp.internal.GlideApp
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.appBarLayout
import kotlinx.android.synthetic.main.fragment_article.collapsingToolbar
import kotlinx.coroutines.launch

class ArticleFragment : Fragment() {

    private lateinit var repositoryImpl: NewsRepositoryImpl
    private lateinit var dataSourceImpl: NewsNetworkDataSourceImpl
    private lateinit var api: NewsApi
    private lateinit var conn: ConnectivityInterceptorImpl
    private lateinit var viewModel: ArticleViewModel
    private lateinit var image: ImageView
    private lateinit var button: Button
    private lateinit var observerNewsArticle: Observer<Article>

    private var article: Article? = null
    private var saving = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_article, container, false)

        conn = ConnectivityInterceptorImpl(activity!!.applicationContext)
        api = NewsApi(conn)
        dataSourceImpl = NewsNetworkDataSourceImpl(api)
        repositoryImpl = NewsRepositoryImpl(dataSourceImpl)
        image = root.findViewById(R.id.header)
        button = root.findViewById(R.id.save_button)

        article = arguments!!.getParcelable("article")

        viewModel = ViewModelProvider(
            this,
            ArticleViewModelFactory(repositoryImpl, activity!!.application)
        ).get(ArticleViewModel::class.java)


        GlideApp.with(activity!!.applicationContext)
            .load(article!!.urlToImage)
            .into(image)
        button.setOnClickListener {
            if (saving){
                viewModel.insert(article)
            } else {
                viewModel.delete(article)
            }
        }
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observerNewsArticle = Observer { newsSource ->
            if (newsSource != null) {
                saving = false
                button.text = "Delete"
            } else {
                saving = true
                button.text = "Save"
            }
        }
        article_text.text = article!!.content
        setToolbar()
        loadNews()
    }

    private fun loadNews() {
        lifecycleScope.launch {
            viewModel.getArticle(article!!.id).observe(viewLifecycleOwner, observerNewsArticle)
        }
    }

    private fun setToolbar() {
        var isShow = true
        var scrollRange = -1
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar.title = article!!.title
                isShow = true
            } else if (isShow) {
                collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

}
