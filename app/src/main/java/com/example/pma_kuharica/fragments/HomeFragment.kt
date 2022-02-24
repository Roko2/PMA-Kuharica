package com.example.pma_kuharica.fragments

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.HomePageRandomFoodAdapter
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.HintsResults
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), Callback<HintsResults> {
    private var mRecyclerView: RecyclerView? = null
    private var numberOfPages:Int=0
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    val foodList:MutableList<Hint> = mutableListOf()
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
        view.findViewById<ProgressBar>(R.id.progressHome).visibility=View.VISIBLE
//        while(numberOfPages<=100) {
//            val apiUrl: String = String.format(
//                "/api/food-database/v2/parser?session=%s&app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=%s",
//                numberOfPages, resources.getString(R.string.searchItems)
//            )
//            ApiManager.getNewInstance()?.service()?.getFood(apiUrl)?.enqueue(this)
//            numberOfPages += 20
//        }
    }

    override fun onResponse(@NonNull call: Call<HintsResults>,@NonNull response: Response<HintsResults>) {
        if (response.isSuccessful && response.body() != null) {
            val hindData:HintsResults = response.body()!!
            for(item in hindData.mResults!!){
                if(item.food?.image!=null && item.food.image.takeLast(3)!="png" && item.food.image!=""){
                    foodList.add(item)
                }
            }
            mRecyclerView =
                view?.findViewById<View>(R.id.recyclerViewRecommendedRecipes) as RecyclerView?
            mLayoutManager = GridLayoutManager(context, 3)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = HomePageRandomFoodAdapter(
                foodList as ArrayList<Hint>
            )
            mRecyclerView!!.adapter = mAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.recycler_spacing) / 2

            mRecyclerView!!.setPadding(spacing, spacing, spacing, spacing)
            mRecyclerView!!.clipToPadding = false
            mRecyclerView!!.clipChildren = false
            mRecyclerView!!.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                    outRect.set(spacing, spacing, spacing, spacing)
                }
            })
        }
        view?.findViewById<ProgressBar>(R.id.progressHome)?.visibility=View.INVISIBLE
    }
    override fun onFailure(@NonNull call: Call<HintsResults>,@NonNull t: Throwable) {
        t.printStackTrace()
    }
}