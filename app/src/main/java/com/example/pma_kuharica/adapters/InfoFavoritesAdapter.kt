package com.example.pma_kuharica.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pma_kuharica.fragments.FavoritesFood
import com.example.pma_kuharica.fragments.FavoritesRecipe

@Suppress("DEPRECATION")
internal class InfoFavoritesAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                FavoritesRecipe()
            }
            1 -> {
                FavoritesFood()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}