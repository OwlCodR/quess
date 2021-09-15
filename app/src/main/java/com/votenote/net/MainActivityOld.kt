package com.votenote.net

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration

/*fun //log(context: Context?, text: String?) {
    *//* @TODO Debug this
    if (text != null) {
        val MAX_LENGTH = 3000
        if (text.length > MAX_LENGTH) {
            val firstPart = text.split("^.{1, $MAX_LENGTH}")[0]
            val secondPart = text.replace(firstPart, "")
            Log.d("VoteNote_Debug", "${context?.javaClass?.simpleName} | ${firstPart}")

            //log(context, secondPart)
        } else {
            Log.d("VoteNote_Debug", "${context?.javaClass?.simpleName} | $text")
        }
    }*//*

    Log.d("VoteNote_Debug", "${context?.javaClass?.simpleName} | $text")
}*/

class MainActivityOld : ComponentActivity() {
    val APP_PREFERENCE = "APP_PREFERENCE"

    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ////log(this, "onCreate()")

//        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(activityMainBinding.root)
//
//        setSupportActionBar(activityMainBinding.appBarMain.toolbar)

        sharedPreferences = getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)
        setContent {
            //MainScreen()
        }

//        val drawerLayout: DrawerLayout = activityMainBinding.drawerLayout
//        val navView: NavigationView = activityMainBinding.navView
//        navController = findNavController(R.id.nav_host_fragment_content_main)
//
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_polls, R.id.nav_profile, R.id.nav_search
//            ), drawerLayout
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.action_settings -> {
//
//            }
//            R.id.action_logout -> {
//                sharedPreferences
//                    .edit()
//                    .putBoolean("loggedIn", false)
//                    .apply()
//
//                // @TODO Make logout request
//
//                val intent = Intent(this, AuthActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//
//        return true
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}

/*
@Composable
fun MainScreen() {
    Text("Hello")
}
*/

/*
@Preview(name = "Main")
@Composable
fun MainScreenPreview() {
    MainScreen()
}*/
