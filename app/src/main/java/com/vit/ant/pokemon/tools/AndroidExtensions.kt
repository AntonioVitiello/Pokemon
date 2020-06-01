package com.vit.ant.pokemon.tools

import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.vit.ant.pokemon.PokemonApplication
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.tools.Utils.hideLoading
import com.vit.ant.pokemon.tools.Utils.showLoading
import io.reactivex.Single
import java.lang.ref.WeakReference

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

fun <T> Single<T>.manageLoading(weakActivity: WeakReference<FragmentActivity>, loadingText: String? = null): Single<T> {
    return compose { upstream ->
        upstream
            .doOnSubscribe {
                showLoading(weakActivity, loadingText)
            }
            .doOnDispose {
                hideLoading(weakActivity)
            }
            .doOnError {
                hideLoading(weakActivity)
            }
            .doOnSuccess {
                hideLoading(weakActivity)
            }
    }
}

fun <T> Single<T>.manageLoading(showProgressLiveData: MutableLiveData<SingleEvent<Boolean>>): Single<T> {
    return compose { upstream ->
        upstream
            .doOnSubscribe {
                showProgressLiveData.postValue(SingleEvent(true))
            }
            .doOnDispose {
                showProgressLiveData.postValue(SingleEvent(false))
            }
            .doOnError {
                showProgressLiveData.postValue(SingleEvent(false))
            }
            .doOnSuccess {
                showProgressLiveData.postValue(SingleEvent(false))
            }
    }
}

fun FragmentActivity.closeKeyboard() {
    val viewFocus = currentFocus ?: return
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(viewFocus.applicationWindowToken, 0)
}

val Float.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Float.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline fun <T : EditText> T.onTextChanged(crossinline listener: (String?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            //do nothing
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.invoke(s?.toString())
        }
    })
}

fun ImageView.rotate360(durationMillis: Long = 1000, repeatCount: Int = Animation.INFINITE) {
    val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    animation.duration = durationMillis
    animation.repeatCount = repeatCount
    animation.repeatMode = Animation.RESTART
    animation.interpolator = LinearInterpolator()
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {}
    })
    startAnimation(animation)
}

//TextView, EditText extensions
fun <T : TextView> T.typedText() = text?.toString() ?: ""
fun <T : TextView> T.isEmpty() = text?.isEmpty() ?: true
fun <T : TextView> T.isNotEmpty() = text?.isNotEmpty() ?: false
fun <T : TextView> T.setHtml(@StringRes htmlRes: Int) {
    setHtml(PokemonApplication.applicationContext.getString(htmlRes))
}

fun <T : TextView> T.setHtml(html: String) {
    text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun <T : EditText> T.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(typedText()).matches()
}

fun <T : EditText> T.isValidIpAddress(): Boolean {
    return Patterns.IP_ADDRESS.matcher(typedText()).matches()
}

fun <T : EditText> T.isValidWebUrl(): Boolean {
    return Patterns.WEB_URL.matcher(typedText()).matches()
}

fun <T : EditText> T.isValidPhone(): Boolean {
    return Patterns.PHONE.matcher(typedText()).matches()
}

inline fun <reified T> Fragment.implementedInterface(): T? {
    return when {
        parentFragment is T -> parentFragment as T
        activity is T -> activity as T
        else -> {
            Log.e(tag, "Cannot trigger interface methods cause the caller doesn't implement the interface ${T::class.java}")
            null
        }
    }
}
