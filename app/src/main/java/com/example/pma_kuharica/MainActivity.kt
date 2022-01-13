package com.example.pma_kuharica

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.Menu
import android.view.MenuItem
import android.view.SearchEvent
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pma_kuharica.adapters.ScreenSlidePagerAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.pma_kuharica.fragments.HomeFragment
import com.example.pma_kuharica.fragments.SearchFragment


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
                override fun onQueryTextSubmit(query: String): Boolean {
                    findViewById<ViewPager2>(R.id.mainViewPager).setBackgroundResource(R.color.fui_transparent)
                    val searchFragment:SearchFragment= SearchFragment()
                    val transaction:FragmentTransaction=supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView,searchFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        searchView.setOnQueryTextListener(queryTextListener);
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


