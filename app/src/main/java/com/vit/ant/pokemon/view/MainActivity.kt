package com.vit.ant.pokemon.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vit.ant.pokemon.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Vitiello Antonio
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}