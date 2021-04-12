package com.vit.ant.pokemon.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vit.ant.pokemon.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Vitiello Antonio
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var backPressedOnce = false
    private val mHandler = Handler(Looper.getMainLooper())


    companion object {
        const val BACK_PRESSED_DELAY_MILLIS = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments[0].childFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            if (backPressedOnce) {
                finish()
            } else {
                backPressedOnce = true
                Toast.makeText(this, getString(R.string.press_two_times), Toast.LENGTH_SHORT).show()
                mHandler.postDelayed({ backPressedOnce = false }, BACK_PRESSED_DELAY_MILLIS)
            }
        }
    }

}