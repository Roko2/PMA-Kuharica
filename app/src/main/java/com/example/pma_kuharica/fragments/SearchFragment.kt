package com.example.pma_kuharica.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.FoodRecyclerViewAdapter
import com.example.pma_kuharica.R
import java.util.ArrayList


class SearchFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private val myDataSet: MutableList<Any?> = ArrayList()

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
        if (myDataSet.size == 0) {
            myDataSet.add("Studenti")
        }
//        mRecyclerView = getView()?.findViewById<View>(R.id.recyclerViewFood) as RecyclerView?
//        mLayoutManager = LinearLayoutManager(context)
//        mRecyclerView!!.layoutManager = mLayoutManager
//        mAdapter = FoodRecyclerViewAdapter(myDataSet)
//        mRecyclerView!!.adapter = mAdapter
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
}