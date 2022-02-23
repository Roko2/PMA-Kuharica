package com.example.pma_kuharica.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.HomePageRandomFoodAdapter
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.HintsResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), Callback<HintsResults>{
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val headerImage=view.findViewById<ImageView>(R.id.imageView)
        headerImage.adjustViewBounds = true
        headerImage.scaleType = ImageView.ScaleType.CENTER_CROP
        val apiUrl:String=String.format("/api/food-database/v2/parser?app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=%s",
            resources.getString(R.string.searchItems))
        ApiManager.getNewInstance()?.service()?.getFood(apiUrl)?.enqueue(this)
    }

    override fun onResponse(call: Call<HintsResults>, response: Response<HintsResults>) {
        mRecyclerView =
            view?.findViewById<View>(R.id.recyclerViewRecommendedRecipes) as RecyclerView?
        mLayoutManager = LinearLayoutManager(context)
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = HomePageRandomFoodAdapter(
            context as AppCompatActivity
        )
        mRecyclerView!!.adapter = mAdapter
    }

    override fun onFailure(call: Call<HintsResults>, t: Throwable) {
        t.printStackTrace()
    }
}