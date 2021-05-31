package com.vit.ant.pokemon.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.DialogFragment
import com.vit.ant.pokemon.R

/**
 * Created by Vitiello Antonio
 */
abstract class FullScreenDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //This is the filter
                if (event.action != KeyEvent.ACTION_DOWN) {
                    true
                } else {
                    onBackPressed()
                    if (isCancelable)
                        dismiss()
                    true // pretend we've processed it
                }
            } else {
                false // pass on to be processed rounded_layout_card_operations normal
            }
        }
    }

    protected open fun onBackPressed() {
        //do nothing
    }

}