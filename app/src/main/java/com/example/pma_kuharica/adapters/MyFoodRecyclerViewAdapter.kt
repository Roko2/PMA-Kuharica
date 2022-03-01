package com.example.pma_kuharica.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyFoodRecyclerViewAdapter (oFood: ArrayList<Food>, context: AppCompatActivity,isRecipeFoodUpdate:Boolean,isFavorite:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var food: ArrayList<Food> = oFood
    private var bIsRecipeFoodUpdate:Boolean=isRecipeFoodUpdate
    private var bIsFoodFavorite:Boolean=isFavorite
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
        if(bIsRecipeFoodUpdate && !bIsFoodFavorite) {
            holder.btnDeleteFood.setOnClickListener {
                    dbReference.child(mAuth.currentUser!!.uid).child("Food")
                        .child(food[holder.adapterPosition].foodId).removeValue()
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Food")
                    .child(food[holder.adapterPosition].foodId).removeValue()
                notifyItemRemoved(holder.adapterPosition)
            }
        }
        else if(bIsFoodFavorite && bIsRecipeFoodUpdate){
            holder.btnFavoriteFood.visibility=View.GONE
            holder.btnDeleteFood.setOnClickListener {
                if(food[holder.adapterPosition].foodId.take(4) != context.resources.getString(R.string.apiStringFood)) {
                    dbReference.child(mAuth.currentUser!!.uid).child("Food")
                        .child(food[holder.adapterPosition].foodId).child(
                        "favorite"
                    ).setValue(false)
                }
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Food")
                    .child(food[holder.adapterPosition].foodId).removeValue()
                notifyItemRemoved(holder.adapterPosition)
            }
        }
        else{
            holder.btnFavoriteFood.visibility=View.GONE
            holder.btnDeleteFood.setOnClickListener {
                food.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                if(food.size==0){
                    context.findViewById<TextView>(R.id.noIngrTxt).visibility=View.VISIBLE
                }
            }
        }
        holder.btnMyFoodNutrients.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment()
            val mBundle = Bundle()
            mBundle.putSerializable("nutrients",oNutrients)
            modalBottomSheet.arguments = mBundle
            modalBottomSheet.show(context.supportFragmentManager, BottomSheetFragment.TAG)
        }
        holder.btnFavoriteFood.setOnClickListener{
            if (!holder.btnFavoriteFood.isChecked) {
                food[holder.adapterPosition].isFavorite=false
                dbReference.child(mAuth.currentUser!!.uid).child("Food").child(food[holder.adapterPosition].foodId).setValue(food[holder.adapterPosition])
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Food").child(food[holder.adapterPosition].foodId).removeValue()
            }
            else{
                food[holder.adapterPosition].isFavorite=true
                dbReference.child(mAuth.currentUser!!.uid).child("Food").child(food[holder.adapterPosition].foodId).setValue(food[holder.adapterPosition])
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Food").child(food[holder.adapterPosition].foodId).setValue(food[holder.adapterPosition])
            }
        }
        holder.btnFavoriteFood.isChecked=food[holder.adapterPosition].isFavorite
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
        var btnFavoriteFood: CheckBox=foodView.findViewById(R.id.starFood)
    }

    override fun getItemCount(): Int {
        return food.size
    }
}