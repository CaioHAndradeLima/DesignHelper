package com.gizmin.bitstore.knowbitstore

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.gizmin.bitstore.knowbitstore.KnowBitStorePageFragment
import com.gizmin.bitstore.R
import java.lang.IllegalStateException

class KnowBitStorePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        private const val TOTAL_ITEMS = 3
        const val LAST_POSITION = TOTAL_ITEMS - 1
    }

    override fun getItem(position: Int): Fragment {
        val kbspd = getKnowBitStorePageData(position)
        return KnowBitStorePageFragment.newInstance(kbspd.message, kbspd.photo, position == LAST_POSITION)
    }

    private fun getKnowBitStorePageData(position: Int): KnowBitStorePageData {
        return when (position) {
            0 -> KnowBitStorePageData(R.string.know_bitstore_carousel_1, R.drawable.ic_like_click)
            1 -> KnowBitStorePageData(R.string.know_bitstore_carousel_2, R.drawable.ic_economize)
            2 -> KnowBitStorePageData(R.string.know_bitstore_carousel_3, R.drawable.ic_coins_264)
            else -> throw IllegalStateException()
        }
    }

    override fun getCount(): Int {
        return TOTAL_ITEMS
    }
}

internal class KnowBitStorePageData(
    @StringRes val message: Int,
    @DrawableRes val photo: Int
)
