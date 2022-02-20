package com.example.pma_kuharica.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.IngredientsRecyclerViewAdapter
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.HintsResults
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetFragmentAddIngr : BottomSheetDialogFragment(), Callback<HintsResults> {
    private lateinit var foodData: HintsResults
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_ingr, container, false)
    }
    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnSearchIngr).setOnClickListener {
            val query=view.findViewById<TextInputLayout>(R.id.inptSearchIngr).editText?.text.toString()
            val apiUrl:String=String.format("/api/food-database/v2/parser?app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=%s",query)
            ApiManager.getNewInstance()?.service()?.getFood(apiUrl)?.enqueue(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().findViewById<SearchView>(R.id.action_search).clearFocus()
    }

    override fun onResponse(call: Call<HintsResults>, response: Response<HintsResults>) {
        if (response.isSuccessful && response.body() != null) {
            foodData = response.body()!!
            mRecyclerView = view?.findViewById<View>(R.id.recyclerViewSearchIngr) as RecyclerView?
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = IngredientsRecyclerViewAdapter(foodData.mResults!!, context as AppCompatActivity)
            mRecyclerView!!.adapter = mAdapter
        }
    }

    override fun onFailure(call: Call<HintsResults>, t: Throwable) {
        t.printStackTrace()
    }
}