package com.mindyapps.android.newsapp

import android.app.Application
import com.mindyapps.android.newsapp.data.db.NewsDatabase
import com.mindyapps.android.newsapp.data.network.*
import com.mindyapps.android.newsapp.data.repository.NewsRepository
import com.mindyapps.android.newsapp.data.repository.NewsRepositoryImpl
import com.mindyapps.android.newsapp.ui.article.ArticleViewModelFactory
import com.mindyapps.android.newsapp.ui.dashboard.DashboardViewModelFactory
import com.mindyapps.android.newsapp.ui.favourites.FavouritesViewModelFactory
import com.mindyapps.android.newsapp.ui.search.SearchViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NewsApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy{
        import(androidXModule(this@NewsApplication))

        bind() from singleton { NewsDatabase(instance()) }
        bind() from singleton {instance<NewsDatabase>().newsDao()}
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton {NewsApi(instance())}
        bind<NewsNetworkDataSource>() with singleton { NewsNetworkDataSourceImpl(instance()) }
        bind<NewsRepository>() with singleton { NewsRepositoryImpl(instance()) }
        bind() from provider { DashboardViewModelFactory(instance()) }
        bind() from provider { FavouritesViewModelFactory(instance(), instance()) }
        bind() from provider { SearchViewModelFactory(instance()) }
        bind() from provider { ArticleViewModelFactory(instance(), instance()) }
    }
}