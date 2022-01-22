package com.example.pma_kuharica.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.pma_kuharica.R
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.HintsResults
import com.example.pma_kuharica.interfaces.HomeInterface
import com.example.pma_kuharica.interfaces.IngredientInterface
import com.google.android.gms.auth.api.credentials.HintRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    var homeListener: HomeInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onDestroy() {
        super.onDestroy()
        homeListener = null
    }

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val headerImage=view.findViewById<ImageView>(R.id.imageView)
        headerImage.adjustViewBounds = true
        headerImage.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}