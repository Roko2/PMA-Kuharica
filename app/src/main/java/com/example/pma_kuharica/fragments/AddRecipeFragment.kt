package com.example.pma_kuharica.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.FoodRecyclerViewAdapter
import com.example.pma_kuharica.adapters.IngredientsRecyclerViewAdapter
import com.example.pma_kuharica.adapters.MyFoodRecyclerViewAdapter
import com.example.pma_kuharica.adapters.MyRecipeRecyclerViewAdapter
import com.example.pma_kuharica.classes.*
import com.example.pma_kuharica.interfaces.IngredientInterface
import com.example.pma_kuharica.interfaces.MainInterface
import com.example.pma_kuharica.interfaces.RecipeInterface
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.time.Duration
import java.util.ArrayList


class AddRecipeFragment : Fragment(),RecipeInterface {
    var data:Any?=null
    val ingredientList: MutableList<Food> = mutableListOf()
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference =database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
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
        val recipeName=view.findViewById<TextInputLayout>(R.id.recipeName)
        val recipeDescription=view.findViewById<TextInputLayout>(R.id.editTxtRecipeDesc)
        view.findViewById<Button>(R.id.btnAddRecipe).setOnClickListener {
            recipeDescription.editText?.text.toString()
            val recipeIngredients:ArrayList<Food> = ingredientList as ArrayList<Food>
            if(recipeName.editText?.text.toString().isNullOrEmpty()){
                recipeName.isErrorEnabled=true
                recipeName.error = "Recipe name is required"
            }
            else if(recipeIngredients.size==0){
                recipeName.isErrorEnabled=false
                Toast.makeText(context,"You have to add some ingredients",Toast.LENGTH_SHORT).show()
            }
            else{
                val newNode=dbReference.child(mAuth.currentUser!!.uid).child("Recipe").push().key.toString()
                val recipe= Recipe(recipeId = newNode, name = recipeName.editText?.text.toString(), food = recipeIngredients, description = recipeDescription.editText?.text.toString())
                dbReference.child(mAuth.currentUser!!.uid).child("Recipe").child(newNode).setValue(recipe)
                EventBus.getDefault().post(MainService(1,true))
                recipeName.editText?.text?.clear()
                recipeDescription.editText?.text?.clear()
                recipeIngredients.clear()
            }
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
        mRecyclerView = view?.findViewById<View>(R.id.recyclerViewAddNewRecipe) as RecyclerView?
        mLayoutManager = LinearLayoutManager(context)
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = MyFoodRecyclerViewAdapter(
            ingredientList as ArrayList<Food>,
            context as AppCompatActivity,
            false
        )
        mRecyclerView!!.adapter = mAdapter
        view?.findViewById<ProgressBar>(R.id.progress)?.visibility=View.GONE
    }

    override fun GetRecipe(oRecipe: Recipe) {
        EventBus.getDefault().post(RecipeService(1, oRecipe))
    }
}