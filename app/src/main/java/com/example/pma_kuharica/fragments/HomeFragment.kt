package com.example.pma_kuharica.fragments

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.HomePageRandomFoodAdapter
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.HintsResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), Callback<HintsResults> {
    private var mRecyclerView: RecyclerView? = null
    private var numberOfPage:Int=20
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: GridLayoutManager? = null
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
        val apiUrl: String = String.format(
            "/api/food-database/v2/parser?app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=%s",
            resources.getString(R.string.searchItems)
        )
        view.findViewById<ProgressBar>(R.id.progressHome)?.visibility=View.VISIBLE
        ApiManager.getNewInstance()?.service()?.getFood(apiUrl)?.enqueue(this)
        var loading = true
        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int
        view.findViewById<RecyclerView>(R.id.recyclerViewRecommendedRecipes).addOnScrollListener(object : RecyclerView.OnScrollListener(),
            Callback<HintsResults> {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager!!.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    pastVisiblesItems = mLayoutManager!!.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            val apiUrl2: String = String.format(
                                "/api/food-database/v2/parser?session=%s&app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=%s",
                                numberOfPage,resources.getString(R.string.searchItems)
                            )
                            ApiManager.getNewInstance()?.service()?.getFood(apiUrl2)?.enqueue(this)
                            numberOfPage+=20
                            loading = true
                        }
                    }
                }
            }

            override fun onResponse(call: Call<HintsResults>, response: Response<HintsResults>) {
                if (response.isSuccessful && response.body() != null) {
                    val hindData:HintsResults = response.body()!!
                    for(item in hindData.mResults!!){
                        if(item.food?.image!=null && item.food.image.takeLast(3)!="png" && item.food.image.isNotEmpty()){
                            foodList.add(item)
                        }
                    }
                    mAdapter?.notifyDataSetChanged()
                }
                view.findViewById<ProgressBar>(R.id.progressHome)?.visibility=View.GONE
            }

            override fun onFailure(call: Call<HintsResults>, t: Throwable) {
                t.printStackTrace()
            }
        })
        view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh).setOnRefreshListener {
            view.findViewById<ProgressBar>(R.id.progressHome).visibility=View.GONE
            view.findViewById<TextView>(R.id.noInternetMsg)?.visibility=View.GONE
            view. findViewById<TextView>(R.id.checkInternetMsg)?.visibility=View.GONE
            view. findViewById<ImageView>(R.id.warningIcon)?.visibility=View.GONE
            view. findViewById<SwipeRefreshLayout>(R.id.swiperefresh).isRefreshing = true
            parentFragmentManager.beginTransaction().detach(this).commit()
            parentFragmentManager.beginTransaction().attach(this).commit()
       }
    }

    override fun onResponse(@NonNull call: Call<HintsResults>,@NonNull response: Response<HintsResults>) {
       view?.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)?.isRefreshing = false
        if (response.isSuccessful && response.body() != null) {
            val hindData:HintsResults = response.body()!!
            for(item in hindData.mResults!!){
                if(item.food?.image!=null && item.food.image.takeLast(3)!="png" && item.food.image!=""){
                    foodList.add(item)
                }
            }
            mRecyclerView =
                view?.findViewById<View>(R.id.recyclerViewRecommendedRecipes) as RecyclerView?
            mLayoutManager = GridLayoutManager(context,3)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = HomePageRandomFoodAdapter(
                foodList as ArrayList<Hint>,
                context as AppCompatActivity
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
        view?.findViewById<ProgressBar>(R.id.progressHome)?.visibility=View.GONE
    }
    override fun onFailure(@NonNull call: Call<HintsResults>,@NonNull t: Throwable) {
        view?.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)?.isRefreshing = false
        view?.findViewById<RecyclerView>(R.id.recyclerViewRecommendedRecipes)?.visibility=View.GONE
        view?.findViewById<ProgressBar>(R.id.progressHome)?.visibility=View.GONE
        view?.findViewById<TextView>(R.id.noInternetMsg)?.visibility=View.VISIBLE
        view?.findViewById<TextView>(R.id.checkInternetMsg)?.visibility=View.VISIBLE
        view?.findViewById<ImageView>(R.id.warningIcon)?.visibility=View.VISIBLE
        t.printStackTrace()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        view?.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)?.isRefreshing = false
    }
}