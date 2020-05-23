package com.vit.ant.pokemon.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.tools.swapFragment

/**
 * Created by Vitiello Antonio
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swapFragment(
            R.id.fragmentContainer, PokemonListFragment.newInstance(),
            PokemonListFragment.TAG
        )
    }

}