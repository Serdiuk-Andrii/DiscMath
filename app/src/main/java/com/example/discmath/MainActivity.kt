package com.example.discmath

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.marginBottom
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.discmath.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

fun View.toggleVisibility() {
    this.visibility = if(this.visibility == View.GONE) View.VISIBLE else View.GONE
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Fragment view
    private lateinit var container: FragmentContainerView
    private lateinit var containerLayoutParams: ConstraintLayout.LayoutParams
    private var marginBottom by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentTime = System.currentTimeMillis()
        installSplashScreen().setKeepOnScreenCondition(condition =
        { System.currentTimeMillis() - currentTime >= 1500 })
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        container = binding.navHostFragmentActivityMain
        containerLayoutParams = container.layoutParams as ConstraintLayout.LayoutParams
        marginBottom = container.marginBottom

        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment!!.findNavController()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val bottomNavigationIds: Set<Int> = setOf(
            R.id.learning_fragment, R.id.start_quizzes_fragment, R.id.assistant_fragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (bottomNavigationIds.contains(destination.id)) {
                navView.visibility = View.VISIBLE
                containerLayoutParams.bottomMargin = marginBottom
            } else {
                navView.visibility = View.GONE
                containerLayoutParams.bottomMargin = 0
            }
        }
        /*val appBarConfiguration = AppBarConfiguration(bottomNavigationIds)
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)

    }



}