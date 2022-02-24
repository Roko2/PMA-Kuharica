package com.example.pma_kuharica.adapters
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.HintsResults
import com.google.android.gms.auth.api.credentials.HintRequest
import java.util.ArrayList
import java.util.concurrent.Executors

class HomePageRandomFoodAdapter(foodList: ArrayList<Hint>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  var foodData:ArrayList<Hint> = foodList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_page_info, parent, false) as View
        return HomeInfoHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as HomeInfoHolder
        val executor = Executors.newSingleThreadExecutor()
        var image: Bitmap? = null
        executor.execute {
            try {
                    val `in` = java.net.URL(foodData[position].food?.image).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    holder.imageHomePage.setImageBitmap(image)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
            var imageHomePage:ImageView=foodView.findViewById(R.id.homePageImage)
    }
}

