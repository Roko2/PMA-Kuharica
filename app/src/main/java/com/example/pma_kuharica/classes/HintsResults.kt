package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class HintsResults : Serializable {
    @SerializedName("hints")
    val mResults: ArrayList<Hint>? = null
    @SerializedName("_links")
    val mNextPage: Links? = null
}