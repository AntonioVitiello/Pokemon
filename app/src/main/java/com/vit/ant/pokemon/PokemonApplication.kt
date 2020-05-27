package com.vit.ant.pokemon

import android.app.Application
import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso


/**
 * Created by Vitiello Antonio
 */
class PokemonApplication : Application() {

    companion object {
        const val IMAGE_URL = "https://pokeres.bastionbot.org/images/pokemon"
        private lateinit var context: Context

        val applicationContext
            get() = context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if(BuildConfig.DEBUG) {
            initPicassoForDebug()
        }
    }

    private fun initPicassoForDebug() {
        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this, Long.MAX_VALUE))
        val picasso = builder.build()
        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
        Picasso.setSingletonInstance(picasso)
    }
}