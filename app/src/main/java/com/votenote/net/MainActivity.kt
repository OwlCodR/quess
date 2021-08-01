package com.votenote.net

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.votenote.net.databinding.ActivityMainBinding
import com.votenote.net.ui.SplashScreenActivity
import com.votenote.net.ui.auth.AuthActivity

fun log(context: Context?, text: String?) {
    // @TODO Debug this
//    if (text != null) {
//        val MAX_LENGTH = 3000
//        if (text.length > MAX_LENGTH) {
//            val firstPart = text.split("^.{1, $MAX_LENGTH}")[0]
//            val secondPart = text.replace(firstPart, "")
//            Log.d("VoteNote_Debug", "${context?.javaClass?.simpleName} | ${firstPart}")
//
//            log(context, secondPart)
//        } else {
//            Log.d("VoteNote_Debug", "${context?.javaClass?.simpleName} | $text")
//        }
//    }

    Log.d("VoteNote_Debug", "${context?.javaClass?.simpleName} | $text")
}

class MainActivity : AppCompatActivity() {
    val APP_PREFERENCE = "APP_PREFERENCE"
    val API_VERSION_TAG = "API_VERSION"

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log(this, "onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        sharedPreferences = getSharedPreferences(
            APP_PREFERENCE,
            Context.MODE_PRIVATE
        )

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_polls, R.id.nav_profile, R.id.nav_search
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {

            }
            R.id.action_logout -> {
                sharedPreferences
                    .edit()
                    .putBoolean("loggedIn", false)
                    .apply()

                // @TODO Make logout request

                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
