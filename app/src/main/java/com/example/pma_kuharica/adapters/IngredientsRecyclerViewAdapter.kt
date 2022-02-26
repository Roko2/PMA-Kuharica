package com.example.pma_kuharica.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.IntentService
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.example.pma_kuharica.fragments.HomeFragment
import com.example.pma_kuharica.fragments.IngredientFragment.Companion.newInstance
import com.example.pma_kuharica.interfaces.IngredientInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus
import java.util.*
import java.util.concurrent.Executors

class IngredientsRecyclerViewAdapter (oFood: ArrayList<Hint>, context: AppCompatActivity,listener:IngredientInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var food: ArrayList<Hint> = oFood
    private var context:AppCompatActivity = context
    private var database:FirebaseDatabase= FirebaseDatabase.getInstance()
    private var dbReference=database.reference
    private var IngredientListener:IngredientInterface=listener
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_search_info, parent, false) as View
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as IngredientViewHolder
        val oNutrients: Nutrients = food[position].food?.nutrients!!
        holder.myFoodSearchName.text = food[position].food?.label
        holder.myCategorySearchName.text = food[position].food?.category
        holder.btnAddIngr.setOnClickListener {
            Toast.makeText(context, "Ingredient is added", Toast.LENGTH_SHORT).show()
            IngredientListener.GetIngredient(food[position].food!!)
        }
        if(food[position].food?.image.isNullOrEmpty()){
           holder.myFoodSearchImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_noimage))
        }
        else {
            Picasso.get().load(food[position].food?.image.toString()).into(holder.myFoodSearchImage)
        }
        holder.btnMyFoodSearchNutrients.setOnClickListener {
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
    internal class IngredientViewHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {
        var myFoodSearchName: TextView = foodView.findViewById(R.id.txtMyFoodNameViewSearch)
        var myCategorySearchName: TextView = foodView.findViewById(R.id.txtMyCategoryNameViewSearch)
        var btnMyFoodSearchNutrients: Button = foodView.findViewById(R.id.myFoodNutrientsBtnSearch)
        var myFoodSearchImage:ImageView=foodView.findViewById(R.id.foodImageSearch)
        var btnAddIngr:ImageButton=foodView.findViewById(R.id.addIngr)
    }

    override fun getItemCount(): Int {
        return food.size
    }
}