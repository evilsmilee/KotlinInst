package ru.nickb.kotlininst.screens.search

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.screens.common.BaseActivity
import ru.nickb.kotlininst.screens.common.ImagesAdapter
import ru.nickb.kotlininst.screens.common.setupAuthGuard
import ru.nickb.kotlininst.screens.common.setupBottomNavigation

class SearchActivity : BaseActivity(), TextWatcher {

    private lateinit var mAdapter: ImagesAdapter
    private lateinit var mViewModel: SearchViewModel
    private var isSearchEntered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupBottomNavigation(1)
        setupAuthGuard {
            mAdapter = ImagesAdapter()
            search_results_recycler.layoutManager = GridLayoutManager(this, 3)
            search_results_recycler.adapter = mAdapter
            mViewModel = initViewModel()
            mViewModel.posts.observe(this, Observer{it?.let {posts ->
                mAdapter.updateImages(posts.map { it.image })
            }})
            search_input.addTextChangedListener(this)
            mViewModel.setSearchText("")
        }
    }

    override fun afterTextChanged(s: Editable?) {
       if (!isSearchEntered) {
           isSearchEntered = true
           Handler().postDelayed({
               isSearchEntered = false
               mViewModel.setSearchText(search_input.text.toString())
           }, 500)

       }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
