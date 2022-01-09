package com.example.pma_kuharica.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

class HomeFragment : Fragment(), Callback<HintsResults> {
    var homeListener: HomeInterface? = null
    lateinit var hintData: HintsResults
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
        ApiManager.getNewInstance()?.service()?.getFood("/api/food-database/v2/parser?app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=bread")?.enqueue(this)
    }

    override fun onResponse(@NonNull call: Call<HintsResults>,@NonNull response: Response<HintsResults>) {
        if (response.isSuccessful && response.body() != null) {
            hintData = response.body()!!
        }
    }

    override fun onFailure(call: Call<HintsResults>, t: Throwable) {
        t.printStackTrace()
    }
}