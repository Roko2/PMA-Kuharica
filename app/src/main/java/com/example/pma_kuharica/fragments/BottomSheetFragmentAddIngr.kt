package com.example.pma_kuharica.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.IngredientsRecyclerViewAdapter
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.HintsResults
import com.example.pma_kuharica.classes.IntentService
import com.example.pma_kuharica.interfaces.IngredientInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetFragmentAddIngr : BottomSheetDialogFragment(), Callback<HintsResults>,IngredientInterface {
    private lateinit var foodData: HintsResults
    val ingredientList:MutableList<Food> = mutableListOf()
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_ingr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ProgressBar>(R.id.progress).visibility=View.GONE
        view.findViewById<Button>(R.id.btnSearchIngr).setOnClickListener {
            val query=view.findViewById<TextInputLayout>(R.id.inptSearchIngr).editText?.text.toString()
            if(query!="") {
                view.findViewById<ProgressBar>(R.id.progress).visibility=View.VISIBLE
                val apiUrl: String = String.format(
                    "/api/food-database/v2/parser?app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=%s",
                    query
                )
                ApiManager.getNewInstance()?.service()?.getFood(apiUrl)?.enqueue(this)
            }
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
    override fun onDetach() {
        super.onDetach()
        requireActivity().findViewById<SearchView>(R.id.action_search).clearFocus()
    }

    override fun onResponse(call: Call<HintsResults>, response: Response<HintsResults>) {
        if (response.isSuccessful && response.body() != null) {
            ingredientList.clear()
            foodData = response.body()!!
            mRecyclerView = view?.findViewById<View>(R.id.recyclerViewSearchIngr) as RecyclerView?
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = IngredientsRecyclerViewAdapter(foodData.mResults!!, context as AppCompatActivity,this)
            mRecyclerView!!.adapter = mAdapter
            view?.findViewById<ProgressBar>(R.id.progress)?.visibility=View.GONE
            if(foodData.mResults!!.size==0){
                view?.findViewById<TextView>(R.id.txtNoResults)?.visibility=View.VISIBLE
            }
        }
    }

    override fun GetIngredient(oFood: Food) {
        EventBus.getDefault().post(IntentService(1, oFood))
        this.dismiss()
    }
    override fun onFailure(call: Call<HintsResults>, t: Throwable) {
        t.printStackTrace()
    }
    companion object {
        const val TAG = "ModalBottomSheet"
    }
}