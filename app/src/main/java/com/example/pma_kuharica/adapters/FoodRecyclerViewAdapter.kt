package com.example.pma_kuharica.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.concurrent.Executors.*

class FoodRecyclerViewAdapter(foodList: ArrayList<Hint>, context:AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataList: ArrayList<Hint> = foodList
    private var context:AppCompatActivity = context
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.food_info, parent, false) as View
            return FoodViewHolder(view)
    }

    @SuppressLint("RestrictedApi", "ResourceType")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as FoodViewHolder
        val oFood: Hint = dataList[holder.adapterPosition]
        var oNutrients:Nutrients= oFood.food!!.nutrients!!
        holder.ingredientTxt.text = oFood.food.label
        holder.categoryTxt.text = oFood.food.category
        holder.btnNutrients.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment()
            val mBundle = Bundle()
            mBundle.putSerializable("nutrients",oNutrients)
            modalBottomSheet.arguments = mBundle
            modalBottomSheet.show(context.supportFragmentManager, BottomSheetFragment.TAG)
        }

        if (oFood.food.image.isNullOrEmpty()) {
            holder.foodImage.setImageResource(R.drawable.ic_noimage)
        } else {
            Picasso.get().load(oFood.food.image.toString()).into(holder.foodImage);
        }
        holder.checkBox.setOnClickListener{
            if (!holder.checkBox.isChecked) {
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Food").child(oFood.food.foodId).removeValue()
            }
            else{
                oFood.food.isFavorite=true
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Food").child(oFood.food.foodId).setValue(oFood.food)
            }
        }
        val db: DatabaseReference = dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Food")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.value != null) {
                        val storedFood: Food? = postSnapshot.getValue<Food>()
                        holder.checkBox.isChecked = storedFood?.foodId==oFood.food.foodId
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal class FoodViewHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {
        var ingredientTxt: TextView = foodView.findViewById(R.id.inptIngredientView)
        var categoryTxt: TextView = foodView.findViewById(R.id.inptCategoryView)
        var foodImage: ImageView = foodView.findViewById(R.id.foodImage)
        var btnNutrients: Button = foodView.findViewById(R.id.btnViewNutrients)
        var checkBox:CheckBox=foodView.findViewById(R.id.star)
    }
}

