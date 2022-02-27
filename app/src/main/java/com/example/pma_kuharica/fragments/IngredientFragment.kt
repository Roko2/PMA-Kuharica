package com.example.pma_kuharica.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.MyFoodRecyclerViewAdapter
import com.example.pma_kuharica.adapters.MyRecipeRecyclerViewAdapter
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.classes.Recipe
import com.example.pma_kuharica.interfaces.IngredientInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.util.*


class IngredientFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    val foodList: MutableList<Food> = mutableListOf()
    val recipeList:MutableList<Recipe> = mutableListOf()
    private var database:FirebaseDatabase=FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference=database.reference
    private val mAuth: FirebaseAuth=FirebaseAuth.getInstance()
    private var btnFood: FloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ingredient, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun newInstance(): IngredientFragment {
            val args = Bundle()
            val fragment = IngredientFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFood= view.findViewById(R.id.floatingBtnFood)
        btnFood?.setOnClickListener{
            showCustomDialogFood()
        }
        val db:DatabaseReference = dbReference.child(mAuth.currentUser!!.uid).child("Food")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.value != null) {
                                val storedFood: Food? = postSnapshot.getValue<Food>()
                                foodList.add(storedFood!!)
                            }
                }
                if(foodList.size>0) {
                    view.findViewById<RecyclerView>(R.id.recyclerViewMyFood).visibility=View.VISIBLE
                    view.findViewById<TextView>(R.id.myFoodEmpty).visibility=View.INVISIBLE
                    mRecyclerView =
                        view.findViewById<View>(R.id.recyclerViewMyFood) as RecyclerView?
                    mLayoutManager = LinearLayoutManager(context)
                    mRecyclerView!!.layoutManager = mLayoutManager
                    mAdapter = MyFoodRecyclerViewAdapter(
                        foodList as ArrayList<Food>,
                        context as AppCompatActivity,
                        true,
                        false
                    )
                    mRecyclerView!!.adapter = mAdapter
                }
                else{
                    view.findViewById<RecyclerView>(R.id.recyclerViewMyFood).visibility=View.INVISIBLE
                    view.findViewById<TextView>(R.id.myFoodEmpty).visibility=View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showCustomDialogFood(){
        val dialog2= Dialog(requireActivity())

        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog2.setCancelable(true)
        dialog2.setContentView(R.layout.add_food)

        dialog2.show()
        val window2: Window? = dialog2.window
        window2?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val categorySpinner = resources.getStringArray(R.array.category)
        val spinner=window2?.findViewById<Spinner>(R.id.spinnerCategory)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, categorySpinner
        )
        spinner?.adapter=adapter
        window2?.findViewById<Button>(R.id.addFood)?.setOnClickListener{
            val userFirebase: FirebaseUser? = FirebaseAuth.getInstance().currentUser

            val newNode=dbReference.child(userFirebase!!.uid).child("Food").push().key.toString()
            val foodChocdf=if(window2.findViewById<TextInputLayout>(R.id.foodCarb).editText?.text.toString().toDoubleOrNull()==null) 0.00 else window2.findViewById<TextInputLayout>(R.id.foodCarb).editText?.text.toString().toDouble()
            val foodEnerc_KCAL=if(window2.findViewById<TextInputLayout>(R.id.foodEnergy).editText?.text.toString().toDoubleOrNull()==null) 0.00 else window2.findViewById<TextInputLayout>(R.id.foodEnergy).editText?.text.toString().toDouble()
            val foodFat=if(window2.findViewById<TextInputLayout>(R.id.foodFat).editText?.text.toString().toDoubleOrNull()==null) 0.00 else window2.findViewById<TextInputLayout>(R.id.foodFat).editText?.text.toString().toDouble()
            val foodFibtg=if(window2.findViewById<TextInputLayout>(R.id.foodFiber).editText?.text.toString().toDoubleOrNull()==null) 0.00 else window2.findViewById<TextInputLayout>(R.id.foodFiber).editText?.text.toString().toDouble()
            val foodProcnt=if(window2.findViewById<TextInputLayout>(R.id.foodProtein).editText?.text.toString().toDoubleOrNull()==null) 0.00 else window2.findViewById<TextInputLayout>(R.id.foodProtein).editText?.text.toString().toDouble()
            val nutrients=Nutrients(chocdf = foodChocdf,
                                    enerc_KCAL = foodEnerc_KCAL,
                                    fat = foodFat,
                                    fibtg  =foodFibtg,
                                    procnt = foodProcnt)

            val food=Food(label = window2.findViewById<TextInputLayout>(R.id.foodName).editText?.text.toString(), category =spinner?.selectedItem.toString(), nutrients = nutrients, foodId = newNode, categoryLabel = "", foodContentsLabel = "", image = "", isFavorite = false)
            dbReference.child(userFirebase.uid).child("Food").child(newNode).setValue(food)
            foodList.add(food)
            Toast.makeText(context, "Food is added", Toast.LENGTH_SHORT).show()
            dialog2.hide()
            mRecyclerView = view?.findViewById<View>(R.id.recyclerViewMyFood) as RecyclerView?
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = MyFoodRecyclerViewAdapter(foodList as ArrayList<Food>, context as AppCompatActivity,true,false)
            mRecyclerView!!.adapter = mAdapter
        }
    }
}

