package com.gizmin.bitstore.knowbitstore

import android.os.Bundle
import com.gizmin.bitstore.BaseActivity
import com.gizmin.bitstore.R
import com.gizmin.bitstore.custom_transform.CarouselEffectTransform
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_know_bitstore.*
import java.util.concurrent.TimeUnit

class KnowBitStoreActivity : BaseActivity() {

    private val viewPager by lazy { viewpager }
    private val tabLayout by lazy { tablayout }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_know_bitstore)
        supportActionBar?.let {
            it.title = getString(R.string.know_bitstore)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        initViewPager()
        initEffectChangePositionAutomatic()
    }

    private fun initEffectChangePositionAutomatic() {
        changePositionViewPagerUntilLastPosition(1)
    }

    private fun changePositionViewPagerUntilLastPosition(position: Int) {
        val time = 5L
        addDisposable(
                Single
                        .fromCallable { Unit }
                        .observeOn(Schedulers.newThread())
                        .delay(time, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                        .filter { position == viewPager.currentItem + 1 }
                        .subscribe({
                            viewPager.currentItem = position

                            if (position < KnowBitStorePageAdapter.LAST_POSITION) {
                                changePositionViewPagerUntilLastPosition(position + 1)
                            }

                        }) {
                            it.printStackTrace()
                        }!!
        )
    }

    private fun initViewPager() {
        viewPager.clipChildren = false
        viewPager.pageMargin = resources.getDimensionPixelOffset(R.dimen.pager_margin)
        viewPager.offscreenPageLimit = 3
        viewPager.setPageTransformer(false, CarouselEffectTransform(this))
        viewPager.adapter = KnowBitStorePageAdapter(supportFragmentManager)

        tabLayout.setupWithViewPager(viewPager, true)
    }

}