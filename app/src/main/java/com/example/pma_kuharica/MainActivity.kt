package com.example.pma_kuharica

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pma_kuharica.api.ApiManager
import com.example.pma_kuharica.classes.HintsResults
import com.example.pma_kuharica.classes.MainService
import com.example.pma_kuharica.fragments.*
import com.example.pma_kuharica.interfaces.MainInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), Callback<HintsResults> {
    private lateinit var hintData: HintsResults
    val fragment2: Fragment = IngredientFragment()
    val fragment1: Fragment = HomeFragment()
    val fragment3: Fragment = InfoFragment()
    val fragment4: Fragment = SearchFragment()
    val fragment5: Fragment = AddRecipeFragment()
    val fragment6: Fragment = FoodFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragment1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        fm.beginTransaction().add(R.id.fragmentContainerView,fragment1, "1").commit()
        fm.beginTransaction().add(R.id.fragmentContainerView, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.fragmentContainerView, fragment5, "5").hide(fragment5).commit()
        fm.beginTransaction().add(R.id.fragmentContainerView, fragment6, "6").hide(fragment6).commit()
        fm.beginTransaction().add(R.id.fragmentContainerView, fragment3, "3").hide(fragment3).commit()
        val bottomNavigationView:BottomNavigationView=findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            when (id) {
                R.id.homePage -> {
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ingredientPage -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    return@OnNavigationItemSelectedListener true
                }
                R.id.infoPage -> {
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                    return@OnNavigationItemSelectedListener true
                }
                R.id.foodPage -> {
                    fm.beginTransaction().hide(active).show(fragment6).commit()
                    active = fragment6
                    findViewById<FloatingActionButton>(R.id.floatingBtnRecipe).setOnClickListener{
                        fm.beginTransaction().hide(active).show(fragment5).commit()
                        active = fragment5
                    }
                    return@OnNavigationItemSelectedListener true
                }
                else -> true
            }
        })
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(intentServiceResult: MainService) {
        fm.beginTransaction().hide(active).show(fragment6).commit()
        active = fragment6
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchViewItem:MenuItem=menu.findItem(R.id.action_search)
        val searchManager:SearchManager= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView =searchViewItem.actionView as SearchView
        searchView.queryHint = "Search for ingredient or food..."
        searchView.setOnQueryTextFocusChangeListener { view, isFocused ->
            val toast = Toast(applicationContext)
            if (isFocused) {
                val inflater = layoutInflater
                val layout: View = inflater.inflate(
                    R.layout.search_toast, null
                )
                val text: TextView = layout.findViewById(R.id.toastTxt)
                text.text = "You can search multiple things etc. milk,egg,bread"
                toast.setGravity(Gravity.TOP, 0, 200)
                toast.duration=Toast.LENGTH_LONG
                toast.view = layout
                toast.show()
            }
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    var apiUrl:String=String.format("/api/food-database/v2/parser?app_id=0fe8f86d&app_key=875e22c3d3ec38bd2453e0223a451f16&ingr=%s",query)
                    ApiManager.getNewInstance()?.service()?.getFood(apiUrl)?.enqueue(this@MainActivity)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    return false
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)
       return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout->{
                signOut()
                return true
            }
            R.id.action_search->{
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

        private fun signOut() {
            Firebase.auth.signOut()
            GoogleSignIn.getClient(
                this.applicationContext,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    override fun onBackPressed() {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
    }
    override fun onResponse(@NonNull call: Call<HintsResults>, @NonNull response: Response<HintsResults>) {
        if (response.isSuccessful && response.body() != null) {
            hintData = response.body()!!
            val mBundle = Bundle()
            mBundle.putSerializable("mResults",hintData)
            fragment4.arguments = mBundle
            if(!fragment4.isAdded && fragment4!=active){
                fm.beginTransaction().add(R.id.fragmentContainerView, fragment4, "4").hide(active).commit()
                active=fragment4
            }
            else{
                if(fragment4.isDetached) {
                    fm.beginTransaction().attach(fragment4).commit()
                    fm.beginTransaction().hide(active).show(fragment4).commit()
                    active = fragment4
                }
                else{
                    fm.beginTransaction().detach(fragment4).commit()
                    fm.beginTransaction().attach(fragment4).commit()
                    fm.beginTransaction().hide(active).show(fragment4).commit()
                    active = fragment4
                }
            }
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
    override fun onFailure(call: Call<HintsResults>, t: Throwable) {
        t.printStackTrace()
    }
    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }
}


