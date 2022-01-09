package com.example.pma_kuharica

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pma_kuharica.adapters.ScreenSlidePagerAdapter
import com.example.pma_kuharica.fragments.HomeFragment
import com.example.pma_kuharica.fragments.InfoFragment
import com.example.pma_kuharica.fragments.IngredientFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import androidx.activity.OnBackPressedCallback


class MainActivity : AppCompatActivity() {
    private var mAdapter: ScreenSlidePagerAdapter? = null
    private var mPager: ViewPager2? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        mAdapter = ScreenSlidePagerAdapter(this)
        mPager = findViewById<View>(R.id.mainViewPager) as ViewPager2
        mPager!!.setAdapter(mAdapter)

        bottomNavigation.setOnItemSelectedListener{menuItem ->
        when (menuItem.itemId) {
            R.id.homePage -> {
                mPager!!.setCurrentItem(0, true)
                true
            }
            R.id.ingredientPage -> {
                mPager!!.setCurrentItem(1, true)
                true
            }
            R.id.infoPage -> {
                mPager!!.setCurrentItem(2, true)
                true
            }
        }
        true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        var searchViewItem:MenuItem=menu.findItem(R.id.action_search)
        var searchManager:SearchManager= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView =searchViewItem.actionView as SearchView
        searchView.queryHint = "Search for ingredient..."
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    // This is your adapter that will be filtered
                    Toast.makeText(applicationContext, "textChanged :$newText", Toast.LENGTH_LONG)
                        .show()
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    // **Here you can get the value "query" which is entered in the search box.**
                    Toast.makeText(applicationContext, "searchvalue :$query", Toast.LENGTH_LONG)
                        .show()
                    return true
                }
            }
        return true;
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
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
}

