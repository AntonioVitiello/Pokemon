package com.vit.ant.pokemon.tools

import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.vit.ant.pokemon.PokemonApplication
import com.vit.ant.pokemon.PokemonApplication.Companion.appContext
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.view.ProgressWheelDialog
import java.lang.ref.WeakReference
import java.text.NumberFormat


object Utils {
    private val urlToIdRegex by lazy(LazyThreadSafetyMode.NONE) { "/-?[0-9]+/$".toRegex() }

    fun urlToId(url: String): Int {
        return urlToIdRegex.find(url)!!.value.filter { it.isDigit() || it == '-' }.toInt()
    }

    val oneDecimalFormater: NumberFormat by lazy {
        NumberFormat.getNumberInstance().apply {
            minimumFractionDigits = 1
            maximumFractionDigits = 1
        }
    }

    @ColorInt
    fun getColorByType(type: String?): Int {
        return ContextCompat.getColor(
            appContext,
            when (type) {
                "normal" -> R.color.typeNormal
                "dragon" -> R.color.typeDragon
                "psychic" -> R.color.typePsychic
                "electric" -> R.color.typeElectric
                "ground" -> R.color.typeGround
                "grass" -> R.color.typeGrass
                "poison" -> R.color.typePoison
                "steel" -> R.color.typeSteel
                "fairy" -> R.color.typeFairy
                "fire" -> R.color.typeFire
                "fight" -> R.color.typeFight
                "bug" -> R.color.typeBug
                "ghost" -> R.color.typeGhost
                "dark" -> R.color.typeDark
                "ice" -> R.color.typeIce
                "water" -> R.color.typeWater
                "rock" -> R.color.typeRock
                "flying" -> R.color.typeFlying
                "fighting" -> R.color.typeFighting
                else -> R.color.typeType
            }
        )
    }

    fun showLoading(weakActivity: WeakReference<FragmentActivity>, loadingText: String? = null) {
        weakActivity.get()?.let { activity ->
            activity.closeKeyboard()
            ProgressWheelDialog.show(activity, loadingText)
        }
    }

    fun hideLoading(weakActivity: WeakReference<FragmentActivity>) {
        weakActivity.get()?.let { activity ->
            ProgressWheelDialog.dismiss(activity)
        }
    }

    fun getString(@StringRes resId: Int): String {
        return PokemonApplication.appContext.getString(resId)
    }

}