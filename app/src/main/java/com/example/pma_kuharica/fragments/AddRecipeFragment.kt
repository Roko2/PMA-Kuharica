package com.example.pma_kuharica.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.FoodRecyclerViewAdapter
import com.example.pma_kuharica.adapters.IngredientsRecyclerViewAdapter
import com.example.pma_kuharica.adapters.MyFoodRecyclerViewAdapter
import com.example.pma_kuharica.adapters.MyRecipeRecyclerViewAdapter
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.IntentService
import com.google.android.material.textfield.TextInputLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList


class AddRecipeFragment : Fragment() {
    var data:Any?=null
    val ingredientList: MutableList<Food> = mutableListOf()
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.addIngredient).setOnClickListener {
            val modalBottomSheet = BottomSheetFragmentAddIngr()
            modalBottomSheet.show(parentFragmentManager, BottomSheetFragmentAddIngr.TAG)
        }
        //ucitaj ingrediente iz globalnog objekta ako postoje
        view.findViewById<Button>(R.id.btnAddRecipe).setOnClickListener {
            val recipeName=view.findViewById<TextInputLayout>(R.id.recipeName).editText?.text.toString()
        }
}
    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(intentServiceResult: IntentService) {
        ingredientList.add(intentServiceResult.mResultValue!!)
        view?.findViewById<ProgressBar>(R.id.progress)?.visibility=View.VISIBLE
        view?.findViewById<TextView>(R.id.noIngrTxt)?.visibility=View.INVISIBLE
        val nodeValue:ArrayList<String>? = null
        mRecyclerView = view?.findViewById<View>(R.id.recyclerViewAddNewRecipe) as RecyclerView?
        mLayoutManager = LinearLayoutManager(context)
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = MyFoodRecyclerViewAdapter(
            ingredientList as ArrayList<Food>,
            nodeValue,
            context as AppCompatActivity
        )
        mRecyclerView!!.adapter = mAdapter
        view?.findViewById<ProgressBar>(R.id.progress)?.visibility=View.GONE
    }
}