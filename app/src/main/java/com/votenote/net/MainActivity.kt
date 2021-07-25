package com.votenote.net

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.votenote.net.databinding.ActivityMainBinding
import com.votenote.net.ui.auth.AuthActivity

fun log(context: Context?, text:String) {
    Log.d(MainActivity().LOG_TAG, "${context?.javaClass?.simpleName} | $text")
}

class MainActivity : AppCompatActivity() {

    val START_PREFERENCE = "START_PREF"
    val LOG_TAG = "VoteNote_Debug"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_polls, R.id.nav_profile, R.id.nav_search
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        checkFirstStart()
        log(this, "onCreate!")
    }

    private fun checkFirstStart() {
        val sharedPreference = getSharedPreferences(START_PREFERENCE, Context.MODE_PRIVATE)

        // sharedPreference.edit().clear().apply()

        val isFirst = sharedPreference.getBoolean("isFirst", true)
        val loggedIn = false


        if (isFirst || !loggedIn) {
            sharedPreference
                .edit()
                .putBoolean("isFirst", false)
                .apply()

            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra("isFirst", isFirst)
            startActivity(intent)

            log(this, "First start or not logged in")
        } else
            log(this, "Not first login or already logged in")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        log(this, "onCreateOptionsMenu!")
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}