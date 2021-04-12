package com.vit.ant.pokemon.view

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vit.ant.pokemon.R

/**
 * Created by Vitiello Antonio
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mBackPressedHandler: Handler

    companion object {
        const val BACK_PRESSED_WHAT = 1
        const val BACK_PRESSED_DELAY_MILLIS = 1000L
        var canExit = false

        class BackPressedHandler : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    BACK_PRESSED_WHAT -> {
                        canExit = false
                    }
                    else -> {
                        //do nothing
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBackPressedHandler = BackPressedHandler()
    }

    override fun onBackPressed() {
        if (canExit) {
            super.onBackPressed()
        } else {
            canExit = true
            Toast.makeText(this, getString(R.string.press_two_times), Toast.LENGTH_SHORT).show()
        }
        mBackPressedHandler.sendEmptyMessageDelayed(BACK_PRESSED_WHAT, BACK_PRESSED_DELAY_MILLIS)
    }

}