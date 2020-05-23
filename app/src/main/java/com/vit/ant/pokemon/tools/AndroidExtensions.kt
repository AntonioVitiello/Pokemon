package com.vit.ant.pokemon.tools

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.tools.Utils.hideLoading
import com.vit.ant.pokemon.tools.Utils.showLoading
import io.reactivex.Single

/**
 * Created by Vitiello Antonio
 */
fun Fragment.addToStackFragment(@IdRes fragmentContainer: Int, fragment: Fragment, bsName: String) {
    val transaction = parentFragmentManager.beginTransaction()
    transaction.setCustomAnimations(
        R.anim.fade_in,
        R.anim.fade_out,
        R.anim.fade_in,
        R.anim.fade_out
    )
    transaction.replace(fragmentContainer, fragment).addToBackStack(bsName)
    transaction.commit()
}

fun AppCompatActivity.swapFragment(
    @IdRes fragmentContainer: Int,
    fragment: Fragment,
    bsName: String
) {
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    val transaction = supportFragmentManager.beginTransaction()
    transaction.setCustomAnimations(
        R.anim.fade_in,
        R.anim.fade_out,
        R.anim.fade_in,
        R.anim.fade_out
    )

    transaction.replace(fragmentContainer, fragment, bsName)
    transaction.commit()
}

fun <T> Single<T>.manageLoading(activity: FragmentActivity, loadingText: String? = null): Single<T> {
    return compose { upstream ->
        upstream
            .doOnSubscribe {
                showLoading(activity, loadingText)
            }
            .doOnDispose {
                hideLoading(activity)
            }
            .doOnError {
                hideLoading(activity)
            }
            .doOnSuccess {
                hideLoading(activity)
            }
    }
}
