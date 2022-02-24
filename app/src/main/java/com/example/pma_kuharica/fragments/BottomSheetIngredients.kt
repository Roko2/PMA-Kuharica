package com.example.pma_kuharica.fragments

import android.content.Context
import android.os.Bundle
import android.view.FocusFinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.adapters.MyFoodRecyclerViewAdapter
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.classes.Recipe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.ArrayList

class BottomSheetIngredients : BottomSheetDialogFragment() {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    var food: Food?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_ingredients, container, false)
    }
    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db: DatabaseReference = dbReference.child(mAuth.currentUser!!.uid).child("Food")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.value != null) {
                        food = postSnapshot.getValue<Food>()
                    }
                }
                if(food!=null) {
                    val nodeValue:ArrayList<String>?=null
                    view.findViewById<RecyclerView>(R.id.recyclerViewMyFood).visibility=View.VISIBLE
                    view.findViewById<TextView>(R.id.myFoodEmpty).visibility=View.INVISIBLE
                    mRecyclerView = view.findViewById<View>(R.id.recyclerViewMyFood) as RecyclerView?
                    mLayoutManager = LinearLayoutManager(context)
                    mRecyclerView!!.layoutManager = mLayoutManager
                    mAdapter = MyFoodRecyclerViewAdapter(
                        food as ArrayList<Food>,
                        nodeValue as ArrayList<String>,
                        context as AppCompatActivity,
                        true
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

    override fun onDetach() {
        super.onDetach()
        requireActivity().findViewById<SearchView>(R.id.action_search).clearFocus()
    }
}