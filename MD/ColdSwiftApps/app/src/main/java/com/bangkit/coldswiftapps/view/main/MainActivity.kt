package com.bangkit.coldswiftapps.view.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.databinding.ActivityMainBinding
import com.bangkit.coldswiftapps.view.ViewModelFactory
import com.bangkit.coldswiftapps.view.WelcomeActivity
import com.bangkit.coldswiftapps.view.home.HomeFragment
import com.bangkit.coldswiftapps.view.myprofile.MyProfile
import com.bangkit.coldswiftapps.view.myticket.MyTicket
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbarTitle: TextView
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val factory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)


        drawer = binding.drawerLayout
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        toolbarTitle = binding.toolbar.findViewById(R.id.toolbar_title)
        val typeface = ResourcesCompat.getFont(this, R.font.lato_bold)
        toolbarTitle.typeface = typeface

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        val togle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)

        drawer.addDrawerListener(togle)
        togle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)
        togle.syncState()


        if (savedInstanceState == null) {
            replacedFragment(HomeFragment(), getString(R.string.home))
            navigationView.setCheckedItem(R.id.nav_home)
        }

        val headerView = navigationView.getHeaderView(0)
        val headerName = headerView.findViewById<TextView>(R.id.username)
        val headerEmail = headerView.findViewById<TextView>(R.id.user_email)

        mainViewModel.getProfile()

        mainViewModel.profile.observe(this){profile ->
            if(profile != null){
                headerName.text = profile.name
                headerEmail.text = profile.email
            }
        }

        mainViewModel.error.observe(this) {error ->
            showToast(error.toString())
        }
    }

    private fun replacedFragment(fragment: Fragment, title: String) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        toolbarTitle.text = title
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> replacedFragment(HomeFragment(), getString(R.string.home))
            R.id.nav_myticket -> replacedFragment(MyTicket(), getString(R.string.my_tickets))
            R.id.nav_myprofile -> replacedFragment(MyProfile(), getString(R.string.my_profile))
            R.id.nav_logout -> {
                mainViewModel.logout()
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
