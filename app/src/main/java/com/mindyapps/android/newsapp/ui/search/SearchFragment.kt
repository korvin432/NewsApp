package com.mindyapps.android.newsapp.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mindyapps.android.newsapp.R
import com.mindyapps.android.newsapp.data.network.ConnectivityInterceptorImpl
import com.mindyapps.android.newsapp.data.network.NewsApi
import com.mindyapps.android.newsapp.data.network.NewsNetworkDataSourceImpl
import com.mindyapps.android.newsapp.data.repository.NewsRepositoryImpl


class SearchFragment : Fragment(), View.OnFocusChangeListener, SearchView.OnQueryTextListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchView: SearchView
    private lateinit var repositoryImpl: NewsRepositoryImpl
    private lateinit var dataSourceImpl: NewsNetworkDataSourceImpl
    private lateinit var api: NewsApi
    private lateinit var conn: ConnectivityInterceptorImpl


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        conn = ConnectivityInterceptorImpl(activity!!.applicationContext)
        api = NewsApi(conn)
        dataSourceImpl = NewsNetworkDataSourceImpl(api)
        repositoryImpl = NewsRepositoryImpl(dataSourceImpl)
        searchView = root.findViewById(R.id.search_view)

        searchViewModel = ViewModelProvider(
            this, SearchViewModelFactory(repositoryImpl)).get(
            SearchViewModel::class.java
        )
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(context, query, Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false

    }

    override fun onFocusChange(p0: View?, p1: Boolean) {

    }


}
