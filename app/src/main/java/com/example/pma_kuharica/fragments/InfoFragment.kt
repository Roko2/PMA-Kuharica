package com.example.pma_kuharica.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.pma_kuharica.R
import com.example.pma_kuharica.interfaces.InfoInterface
import com.example.pma_kuharica.interfaces.IngredientInterface


class InfoFragment : Fragment() {
    var infoListener: InfoInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_info, container, false)

    override fun onDestroy() {
        super.onDestroy()
        infoListener = null
    }

    companion object {
        fun newInstance(): InfoFragment {
            val args = Bundle()
            val fragment = InfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}