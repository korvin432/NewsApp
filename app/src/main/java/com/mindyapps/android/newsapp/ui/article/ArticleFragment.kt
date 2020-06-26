package com.mindyapps.android.newsapp.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.model.Article
import com.mindyapps.android.newsapp.databinding.FragmentArticleBinding
import com.mindyapps.android.newsapp.internal.Constants.Companion.KEY_ARTICLE
import com.mindyapps.android.newsapp.internal.GlideApp
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ArticleFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ArticleViewModelFactory by instance()
    private lateinit var viewModel: ArticleViewModel
    private lateinit var observerNewsArticle: Observer<List<Article>>
    private var article: Article? = null
    private var isFavourite = false

    private lateinit var binding: FragmentArticleBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false)
        article = requireArguments().getParcelable(KEY_ARTICLE)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleViewModel::class.java)
        binding.article = article



        binding.floatingButton.setOnClickListener {
            isFavourite = if (!isFavourite) {
                viewModel.insert(article)
                true
            } else {
                if (article!!.id != null) {
                    viewModel.delete(article)
                } else {
                    viewModel.deleteLastArticle()
                }
                false
            }
            setButton(isFavourite)
        }
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isFavourite = article!!.id != null
        observerNewsArticle = Observer { newsSource ->
            if (newsSource != null) {
                setButton(isFavourite)
            }
        }
        setTitle()
        setToolbar()
        loadNews()
    }

    private fun setTitle() {
        val content = SpannableString(article!!.title)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        article_title.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article!!.url)))
        }
    }

    private fun setButton(isFavourite: Boolean) {
        if (isFavourite) {
            binding.floatingButton.setImageResource(R.drawable.icon_star_filled)
            binding.floatingButton.drawable.mutate()
                .setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
        } else {
            binding.floatingButton.setImageResource(R.drawable.icon_star)
            binding.floatingButton.drawable.mutate()
                .setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
        }
    }

    private fun loadNews() {
        lifecycleScope.launch {
            viewModel.getArticles().observe(viewLifecycleOwner, observerNewsArticle)
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
                isShow = true
            } else if (isShow) {
                isShow = false
            }
        })
    }

}


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?){
    if (!url.isNullOrEmpty()) {
        try {
            GlideApp.with(view.context)
                .load(url)
                .into(view)
        } catch (ex: Exception) {

        }
    }

}