package com.example.pma_kuharica.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R

class HomePageRandomFoodAdapter(context:AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context:AppCompatActivity = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_page_info, parent, false) as View
        return HomeInfoHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        TODO("")
    }

    internal class HomeInfoHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {
    }
}

