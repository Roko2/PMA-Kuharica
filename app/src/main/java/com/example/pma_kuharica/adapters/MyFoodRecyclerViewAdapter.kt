package com.example.pma_kuharica.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import java.util.concurrent.Executors

class MyFoodRecyclerViewAdapter (oFood: ArrayList<Food>,oNodeKeys:ArrayList<String>?, context: AppCompatActivity,isRecipeFoodUpdate:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var food: ArrayList<Food> = oFood
    private var bIsRecipeFoodUpdate:Boolean=isRecipeFoodUpdate
    private var nodeValue:ArrayList<String>? = oNodeKeys
    private var context:AppCompatActivity = context
    private var database:FirebaseDatabase= FirebaseDatabase.getInstance()
    private var dbReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_food_info, parent, false) as View
        return MyFoodViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as MyFoodViewHolder
        val oNutrients: Nutrients = food[position].nutrients!!
        holder.myFoodName.text = food[position].label
        holder.myCategoryName.text = food[position].category
        if(bIsRecipeFoodUpdate) {
            holder.btnDeleteFood.tag = nodeValue!![position]
            holder.btnDeleteFood.setOnClickListener {
                dbReference.child(mAuth.currentUser!!.uid).child("Food").child(holder.btnDeleteFood.tag.toString()).removeValue()
            }
        }
        else{
            holder.btnDeleteFood.setOnClickListener {
                val result = food.toMutableList()
                result.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition);
                notifyItemRangeChanged(holder.adapterPosition,food.size);
            }
        }
        holder.btnMyFoodNutrients.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment()
            val mBundle = Bundle()
            mBundle.putSerializable("nutrients",oNutrients)
            modalBottomSheet.arguments = mBundle
            modalBottomSheet.show(context.supportFragmentManager, BottomSheetFragment.TAG)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    internal class MyFoodViewHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {
        var myFoodName: TextView = foodView.findViewById(R.id.txtMyFoodNameView)
        var myCategoryName: TextView = foodView.findViewById(R.id.txtMyCategoryNameView)
        var btnMyFoodNutrients: Button = foodView.findViewById(R.id.myFoodNutrientsBtn)
        var btnDeleteFood:ImageButton=foodView.findViewById(R.id.deleteFood)
    }

    override fun getItemCount(): Int {
        return food.size
    }
}