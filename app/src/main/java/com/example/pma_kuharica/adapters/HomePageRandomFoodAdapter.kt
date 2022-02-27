package com.example.pma_kuharica.adapters
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.squareup.picasso.Picasso
import java.nio.channels.Channel

class HomePageRandomFoodAdapter(foodList: ArrayList<Hint>,context:AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  var foodData:ArrayList<Hint> = foodList
    private val context=context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_page_info, parent, false) as View
        return HomeInfoHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as HomeInfoHolder
        Picasso.get().load(foodData[position].food?.image.toString()).into(holder.imageHomePage)
        holder.imageHomePage.setOnClickListener {
            val oNutrients: Nutrients = foodData[position].food?.nutrients!!
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

    override fun getItemCount(): Int {
       return foodData.size
    }

    internal class HomeInfoHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {
            var imageHomePage:ImageButton=foodView.findViewById(R.id.homePageImage)
    }
}

