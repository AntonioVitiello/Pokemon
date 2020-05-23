package com.vit.ant.pokemon.tools

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.vit.ant.pokemon.PokemonApplication.Companion.applicationContext
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.view.ProgressWheelDialog
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
            applicationContext,
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
                else -> R.color.typeType
            }
        )
    }

    fun showLoading(activity: FragmentActivity, loadingText: String? = null) {
        closeKeyboard(activity)
        ProgressWheelDialog.show(activity, loadingText)
    }

    fun hideLoading(activity: FragmentActivity) {
        ProgressWheelDialog.dismiss(activity)
    }

    fun closeKeyboard(activity: Activity?) {
        activity ?: return
        val viewFocus = activity.currentFocus
        viewFocus ?: return
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(viewFocus.getApplicationWindowToken(), 0)
    }

}