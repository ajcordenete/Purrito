package com.aljon.purrito.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aljon.purrito.R
import com.aljon.purrito.ui.favorite.FavoritesFragment
import com.aljon.purrito.ui.feed.cat.CatFeedFragment
import com.aljon.purrito.ui.feed.dog.DogFeedFragment

class TabsAdapter(parentFragment: Fragment): FragmentStateAdapter(parentFragment) {

    val titles = listOf<Int>(R.string.cats_label, R.string.dogs_label, R.string.favorites_label)
    val icons = listOf<Int>(R.drawable.ic_cat, R.drawable.ic_dog, R.drawable.ic_favorite)

    override fun getItemCount(): Int = icons.count()

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            CatFeedFragment()

        } else if(position == 1) {
            DogFeedFragment()

        } else {
            FavoritesFragment()
        }
    }
}