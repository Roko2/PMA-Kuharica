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
import com.example.pma_kuharica.adapters.FoodRecyclerViewAdapter
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
    private var nextHintData:HintsResults?=null
    private var previousPageLinks:MutableList<String> = mutableListOf()
    private lateinit var hintList:HintsResults
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
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnPrevious=view.findViewById<Button>(R.id.btnPrevious)
        hintList = arguments?.getSerializable(
            "mResults"
        ) as HintsResults

        if (hintList.mResults?.size == 0) {
           view.findViewById<TextView>(R.id.txtViewSearchMsg)?.text = "No results"
        } else {
            if(hintList.mNextPage == null){
                btnNext?.visibility = INVISIBLE
                btnPrevious?.visibility = INVISIBLE
            }
            else{
                btnNext?.visibility = VISIBLE
                btnPrevious?.visibility = INVISIBLE
            }
            view.findViewById<TextView>(R.id.txtViewSearchMsg)?.text = "Search results"
            btnNext?.setOnClickListener {
                val apiUrl: String = hintList.mNextPage?.next?.href.toString()
                val substringApi: String = apiUrl.subSequence(22, apiUrl.lastIndex + 1).toString()
                ApiManager.getNewInstance()?.service()?.getFood(substringApi)?.enqueue(this)
                previousPageLinks.add(apiUrl)
                if(previousPageLinks.size>1){
                    btnPrevious?.visibility = VISIBLE
                }
            }
            btnPrevious.setOnClickListener {
                if(previousPageLinks.size>1){
                    val apiUrl: String = previousPageLinks.takeLast(2).first()
                    val substringApi: String = apiUrl.subSequence(22, apiUrl.lastIndex + 1).toString()
                    ApiManager.getNewInstance()?.service()?.getFood(substringApi)?.enqueue(this)
                    previousPageLinks.removeLast()
                    if(previousPageLinks.size==1){
                        btnPrevious?.visibility = INVISIBLE
                    }
                }
                else{
                    btnPrevious?.visibility = INVISIBLE
                    btnNext?.visibility = VISIBLE
                }
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
             nextHintData = response.body()!!
            hintList= nextHintData as HintsResults
            mRecyclerView = view?.findViewById<View>(R.id.recyclerViewFood) as RecyclerView?
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = FoodRecyclerViewAdapter(nextHintData!!.mResults!!, context as AppCompatActivity)
            mRecyclerView!!.adapter = mAdapter
              }
            }

    override fun onFailure(call: Call<HintsResults>, t: Throwable) {
        TODO("Not yet implemented")
    }
}


