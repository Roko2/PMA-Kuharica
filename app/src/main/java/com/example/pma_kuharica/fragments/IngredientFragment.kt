package com.example.pma_kuharica.fragments

import android.app.Dialog
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
    var ingredientListener: IngredientInterface? = null
    private val rotateOpen: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.to_bottom_anim)}
    private val fragmentAddRecipe: Fragment = AddRecipeFragment()
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    val foodList: MutableList<Food> = mutableListOf()
    val recipeList:MutableList<Recipe> = mutableListOf()
    val nodeValue:MutableList<String> = mutableListOf()
    private var database:FirebaseDatabase=FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference=database.reference
    private val mAuth: FirebaseAuth=FirebaseAuth.getInstance()
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var clicked=false
    private var btnFood: FloatingActionButton? = null
    private var btnRecipe : FloatingActionButton? = null
    private var txtFoodFloating: TextView? =null
    private var txtRecipeFloating: TextView? =null
    private var openFloatingButtons:FloatingActionButton?=null
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
        ingredientListener = null
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
        openFloatingButtons=view.findViewById(R.id.btnShowDialog)
        btnFood= view.findViewById(R.id.floatingBtnFood)
        btnRecipe=view.findViewById(R.id.floatingBtnRecipe)
        txtFoodFloating=view.findViewById(R.id.addFoodTxt)
        txtRecipeFloating=view.findViewById(R.id.addRecipeTxt)
        openFloatingButtons?.setOnClickListener{
            onAddButtonClicked()
        }
        btnFood?.setOnClickListener{
            showCustomDialogFood()
        }
        val db:DatabaseReference = dbReference.child(mAuth.currentUser!!.uid).child("Food")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()
                nodeValue.clear()
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.value != null) {
                                val storedFood: Food? = postSnapshot.getValue<Food>()
                                nodeValue.add(postSnapshot.key.toString())
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
                        nodeValue as ArrayList<String>,
                        context as AppCompatActivity
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


        val dbRecipe:DatabaseReference = dbReference.child(mAuth.currentUser!!.uid).child("Recipe")

        dbRecipe.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.value != null) {

                    }
                }
                if(recipeList.size>0) {
                    view.findViewById<RecyclerView>(R.id.recyclerViewMyRecipe).visibility=View.VISIBLE
                    view.findViewById<TextView>(R.id.myRecipeEmpty).visibility=View.INVISIBLE
                    mRecyclerView =
                        view.findViewById<View>(R.id.recyclerViewMyRecipe) as RecyclerView?
                    mLayoutManager = LinearLayoutManager(context)
                    mRecyclerView!!.layoutManager = mLayoutManager
                    mAdapter = MyRecipeRecyclerViewAdapter(
                        recipeList as ArrayList<Recipe>,
                        nodeValue as ArrayList<String>,
                        context as AppCompatActivity
                    )
                    mRecyclerView!!.adapter = mAdapter
                }
                else{
                    view.findViewById<RecyclerView>(R.id.recyclerViewMyRecipe).visibility=View.INVISIBLE
                    view.findViewById<TextView>(R.id.myRecipeEmpty).visibility=View.VISIBLE
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
            val randFoodId=UUID.randomUUID().toString()
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

            val food=Food(label = window2.findViewById<TextInputLayout>(R.id.foodName).editText?.text.toString(), category =spinner?.selectedItem.toString(), nutrients = nutrients, foodId = randFoodId, categoryLabel = "", foodContentsLabel = "", image = "")
            val userFirebase: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            val newNode=dbReference.child(userFirebase!!.uid).child("Food").push().key.toString()
            nodeValue.add(newNode)
            dbReference.child(userFirebase.uid).child("Food").child(newNode).setValue(food)
            foodList.add(food)
            Toast.makeText(context, "Food is added", Toast.LENGTH_SHORT).show()
            dialog2.hide()
            mRecyclerView = view?.findViewById<View>(R.id.recyclerViewMyFood) as RecyclerView?
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView!!.layoutManager = mLayoutManager
            mAdapter = MyFoodRecyclerViewAdapter(foodList as ArrayList<Food>,nodeValue as ArrayList<String>, context as AppCompatActivity)
            mRecyclerView!!.adapter = mAdapter
            onAddButtonClicked()
        }
    }
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }
    private fun setVisibility(clicked:Boolean){
    if(!clicked){
        btnFood?.visibility=View.VISIBLE
        btnRecipe?.visibility=View.VISIBLE
        txtFoodFloating?.visibility=View.VISIBLE
        txtRecipeFloating?.visibility=View.VISIBLE
    }else{
        btnFood?.visibility=View.INVISIBLE
        btnRecipe?.visibility=View.INVISIBLE
        txtFoodFloating?.visibility=View.INVISIBLE
        txtRecipeFloating?.visibility=View.INVISIBLE
        }
    }
    private fun setAnimation(clicked: Boolean){
    if(!clicked){
        btnFood?.startAnimation(fromBottom)
        btnRecipe?.startAnimation(fromBottom)
        txtFoodFloating?.startAnimation(fromBottom)
        txtRecipeFloating?.startAnimation(fromBottom)
        openFloatingButtons?.startAnimation(rotateOpen)
    }else{
        btnFood?.startAnimation(toBottom)
        btnRecipe?.startAnimation(toBottom)
        txtFoodFloating?.startAnimation(toBottom)
        txtRecipeFloating?.startAnimation(toBottom)
        openFloatingButtons?.startAnimation(rotateClose)
    }
    }

    private fun setClickable(clicked: Boolean){
        if(!clicked){
            btnFood?.isClickable=true
            btnRecipe?.isClickable=true
        }else{
            btnFood?.isClickable=false
            btnRecipe?.isClickable=false
        }
    }
}

