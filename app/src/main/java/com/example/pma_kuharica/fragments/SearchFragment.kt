package com.example.pma_kuharica.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.FoodRecyclerViewAdapter
import com.example.pma_kuharica.R
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.HintsResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment(), Callback<HintsResults> {
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnNext=getView()?.findViewById<Button>(R.id.btnNext)
           var hintList = arguments?.getSerializable(
                "mResults"
            ) as HintsResults

            if (hintList.mResults?.size == 0) {
                getView()?.findViewById<TextView>(R.id.txtViewSearchMsg)?.text = "No results"
                btnNext?.visibility=INVISIBLE
            } else {
                getView()?.findViewById<TextView>(R.id.txtViewSearchMsg)?.text = "Search results"
                btnNext?.visibility= VISIBLE
                btnNext?.setOnClickListener{
                    var apiUrl:String=hintList.mNextPage?.next?.href.toString()
                    val substringApi:String = apiUrl.subSequence(22, apiUrl.lastIndex+1).toString()
                    ApiManager.getNewInstance()?.service()?.getFood(substringApi)?.enqueue(this)
                }
                mRecyclerView = getView()?.findViewById<View>(R.id.recyclerViewFood) as RecyclerView?
                mLayoutManager = LinearLayoutManager(context)
                mRecyclerView!!.layoutManager = mLayoutManager
                mAdapter = FoodRecyclerViewAdapter(hintList.mResults!!, context as AppCompatActivity)
                mRecyclerView!!.adapter = mAdapter
            }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun newInstance(): SearchFragment {
            val args = Bundle()
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResponse(call: Call<HintsResults>, response: Response<HintsResults>) {
        if (response.isSuccessful && response.body() != null) {
            var nextHintData:HintsResults = response.body()!!
            mRecyclerView = view?.findViewById<View>(R.id.recyclerViewFood) as RecyclerView?
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = FoodRecyclerViewAdapter(nextHintData.mResults!!, context as AppCompatActivity)
            mRecyclerView!!.adapter = mAdapter
              }
            }

    override fun onFailure(call: Call<HintsResults>, t: Throwable) {
        TODO("Not yet implemented")
    }
}


