package com.example.pma_kuharica.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.MyRecipeRecyclerViewAdapter
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Recipe
import com.example.pma_kuharica.interfaces.IngredientInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class FoodFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    val recipeList:MutableList<Recipe> = mutableListOf()
    val nodeValue:MutableList<String> = mutableListOf()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbRecipe: DatabaseReference = dbReference.child(mAuth.currentUser!!.uid).child("Recipe")

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
}