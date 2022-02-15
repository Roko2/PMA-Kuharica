package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("category")
    val category: String="",
    @SerializedName("categoryLabel")
    val categoryLabel: String?="",
    @SerializedName("foodContentsLabel")
    val foodContentsLabel: String?="",
    @SerializedName("foodId")
    val foodId: String="",
    @SerializedName("image")
    val image: String?="",
    @SerializedName("label")
    val label: String?="",
    @SerializedName("nutrients")
    var nutrients: Nutrients? = null,
    @SerializedName("servingSizes")
    val servingSizes: String?=""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Food

        if (category != other.category) return false
        if (categoryLabel != other.categoryLabel) return false
        if (foodContentsLabel != other.foodContentsLabel) return false
        if (foodId != other.foodId) return false
        if (image != other.image) return false
        if (label != other.label) return false
        if (servingSizes != other.servingSizes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + (categoryLabel?.hashCode() ?: 0)
        result = 31 * result + (foodContentsLabel?.hashCode() ?: 0)
        result = 31 * result + foodId.hashCode()
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (label?.hashCode() ?: 0)
        result = 31 * result + (servingSizes?.hashCode() ?: 0)
        return result
    }
}
