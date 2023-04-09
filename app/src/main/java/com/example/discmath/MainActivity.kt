package com.example.discmath

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.discmath.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

fun View.toggleVisibility() {
    this.visibility = if(this.visibility == View.GONE) View.VISIBLE else View.GONE
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentTime = System.currentTimeMillis()
        installSplashScreen().setKeepOnScreenCondition(condition =
        { System.currentTimeMillis() - currentTime >= 1500 })
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment!!.findNavController()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val bottomNavigationIds: Set<Int> = setOf(
            R.id.learning_fragment, R.id.start_quizzes_fragment, R.id.navigation_notifications
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (bottomNavigationIds.contains(destination.id)) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }
        /*val appBarConfiguration = AppBarConfiguration(bottomNavigationIds)
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)

    }

}