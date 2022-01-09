package com.example.pma_kuharica.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pma_kuharica.fragments.HomeFragment
import com.example.pma_kuharica.interfaces.HomeInterface
import com.example.pma_kuharica.interfaces.InfoInterface
import com.example.pma_kuharica.interfaces.IngredientInterface
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pma_kuharica.classes.Ingredient
import com.example.pma_kuharica.fragments.InfoFragment
import com.example.pma_kuharica.fragments.IngredientFragment

private const val NUM_PAGES = 3

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa),
    HomeInterface, InfoInterface, IngredientInterface {
    private var ingredientData: Ingredient? =null
    override fun setIngredient(ingredient: Ingredient) {
        ingredientData= ingredient
    }

    override fun getIngredient(): Ingredient? {
        return ingredientData
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = HomeFragment()
                (fragment as HomeFragment?)?.homeListener = this
            }
            1 -> {
                fragment = IngredientFragment()
                (fragment as IngredientFragment?)?.ingredientListener = this
            }
            else -> {
                fragment = InfoFragment()
                (fragment as InfoFragment?)?.infoListener = this
            }
        }
        return fragment!!
    }
}
