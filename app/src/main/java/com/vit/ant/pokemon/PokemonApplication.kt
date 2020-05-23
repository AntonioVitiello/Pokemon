package com.vit.ant.pokemon

import android.app.Application
import android.content.Context

/**
 * Created by Vitiello Antonio
 */
class PokemonApplication : Application() {

    companion object {
        lateinit var context: Context
        const val IMAGE_URL = "https://pokeres.bastionbot.org/images/pokemon"

        val applicationContext
            get() = context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}