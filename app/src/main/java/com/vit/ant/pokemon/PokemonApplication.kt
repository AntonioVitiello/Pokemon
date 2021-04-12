package com.vit.ant.pokemon

import android.app.Application
import android.util.Log
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import io.reactivex.plugins.RxJavaPlugins


/**
 * Created by Vitiello Antonio
 */
class PokemonApplication : Application() {

    companion object {
        const val TAG = "PokemonApplication"
        const val IMAGE_URL = "https://pokeres.bastionbot.org/images/pokemon"
        private lateinit var application: Application

        val appContext
            get() = application.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        //RxJava default error handler to avoid app crash
        RxJavaPlugins.setErrorHandler { thr: Throwable -> Log.e(TAG, "Error on RxJava plugin.", thr) }

        if (BuildConfig.DEBUG) {
            initPicassoForDebug()
        }
    }

    private fun initPicassoForDebug() {
        val instance = Picasso.Builder(this).apply {
            downloader(OkHttp3Downloader(this@PokemonApplication, Long.MAX_VALUE))
            indicatorsEnabled(true)
            loggingEnabled(true)
        }.build()
        Picasso.setSingletonInstance(instance)
    }
}