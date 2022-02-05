package com.example.pma_kuharica.fragments

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.pma_kuharica.R
import com.example.pma_kuharica.interfaces.IngredientInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton

class IngredientFragment : Fragment() {
    var ingredientListener: IngredientInterface? = null
    private val rotateOpen: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy{ AnimationUtils.loadAnimation(context,R.anim.to_bottom_anim)}
    private var clicked=false;
    private var btnFood: FloatingActionButton? = null
    private var btnRecipe : FloatingActionButton? = null
    private var txtFoodFloating: TextView? =null
    private var txtRecipeFloating: TextView? =null
    private var openFloatingButtons:FloatingActionButton?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ingredient, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        ingredientListener = null
    }

    companion object {
        fun newInstance(): IngredientFragment {
            val args = Bundle()
            val fragment = IngredientFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openFloatingButtons=view.findViewById(R.id.btnShowDialog)
        btnFood= view.findViewById(R.id.floatingBtnFood)
        btnRecipe=view.findViewById(R.id.floatingBtnRecipe)
        txtFoodFloating=view.findViewById(R.id.addFoodTxt)
        txtRecipeFloating=view.findViewById(R.id.addRecipeTxt)

        openFloatingButtons?.setOnClickListener{
            onAddButtonClicked()
        }
        btnFood?.setOnClickListener{
            showCustomDialog()
        }
    }
    private fun showCustomDialog(){
        val dialog= Dialog(requireActivity())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_recipe)
        dialog.show()
        val window: Window? = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }
    private fun setVisibility(clicked:Boolean){
    if(!clicked){
        btnFood?.visibility=View.VISIBLE
        btnRecipe?.visibility=View.VISIBLE
        txtFoodFloating?.visibility=View.VISIBLE
        txtRecipeFloating?.visibility=View.VISIBLE
    }else{
        btnFood?.visibility=View.INVISIBLE
        btnRecipe?.visibility=View.INVISIBLE
        txtFoodFloating?.visibility=View.INVISIBLE
        txtRecipeFloating?.visibility=View.INVISIBLE
        }
    }
    private fun setAnimation(clicked: Boolean){
    if(!clicked){
        btnFood?.startAnimation(fromBottom)
        btnRecipe?.startAnimation(fromBottom)
        txtFoodFloating?.startAnimation(fromBottom)
        txtRecipeFloating?.startAnimation(fromBottom)
        openFloatingButtons?.startAnimation(rotateOpen)
    }else{
        btnFood?.startAnimation(toBottom)
        btnRecipe?.startAnimation(toBottom)
        txtFoodFloating?.startAnimation(toBottom)
        txtRecipeFloating?.startAnimation(toBottom)
        openFloatingButtons?.startAnimation(rotateClose)
    }
    }

    private fun setClickable(clicked: Boolean){
        if(!clicked){
            btnFood?.isClickable=false
            btnRecipe?.isClickable=false
        }else{
            btnFood?.isClickable=true
            btnRecipe?.isClickable=true
        }
    }
}

