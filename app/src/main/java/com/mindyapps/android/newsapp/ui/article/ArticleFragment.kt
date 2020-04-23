package com.mindyapps.android.newsapp.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.AppBarLayout
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.network.ConnectivityInterceptorImpl
import com.mindyapps.android.newsapp.data.network.NewsApi
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSourceImpl
import com.mindyapps.android.newsapp.data.repository.NewsRepositoryImpl
import com.mindyapps.android.newsapp.internal.GlideApp
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.appBarLayout
import kotlinx.android.synthetic.main.fragment_article.collapsingToolbar

class ArticleFragment : Fragment() {

    private lateinit var repositoryImpl: NewsRepositoryImpl
    private lateinit var dataSourceImpl: NewsNetworkDataSourceImpl
    private lateinit var api: NewsApi
    private lateinit var conn: ConnectivityInterceptorImpl
    private lateinit var viewModel: ArticleViewModel
    private lateinit var image: ImageView

    private lateinit var imageUrl: String
    private lateinit var title: String
    private lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_article, container, false)

        conn = ConnectivityInterceptorImpl(activity!!.applicationContext)
        api = NewsApi(conn)
        dataSourceImpl = NewsNetworkDataSourceImpl(api)
        repositoryImpl = NewsRepositoryImpl(dataSourceImpl)
        image = root.findViewById(R.id.header)

        imageUrl = arguments!!.getString("imageUrl", "")
        title = arguments!!.getString("title", "No title")
        text = arguments!!.getString("text", "No content")
        ViewCompat.setTransitionName(image, imageUrl)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ArticleViewModelFactory(repositoryImpl)).get(ArticleViewModel::class.java)

        setToolbar()
        GlideApp.with(activity!!.applicationContext)
            .load(imageUrl)
            .into(image)
        article_text.text = text
    }

    private fun setToolbar() {
        var isShow = true
        var scrollRange = -1
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar.title = title
                isShow = true
            } else if (isShow) {
                collapsingToolbar.title = " "
                isShow = false
            }
        })
    }

}
