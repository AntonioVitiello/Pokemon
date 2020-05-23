package com.vit.ant.pokemon.viewmodel

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vit.ant.pokemon.model.PokemonDetailsModel
import com.vit.ant.pokemon.model.PokemonModel
import com.vit.ant.pokemon.network.map.mapPokemonDetails
import com.vit.ant.pokemon.repository.PokemonRepository
import com.vit.ant.pokemon.tools.manageLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Created by Vitiello Antonio
 */
class DetailPokemonViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var pokemonDetailsLiveData = MutableLiveData<PokemonDetailsModel>()
    var errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    companion object {
        private const val TAG = "DetailPokemonViewModel"
    }


    fun getPokemonDetails(id: Int, activity: WeakReference<FragmentActivity>) {
        compositeDisposable.add(
                PokemonRepository.getPokemonDetails(id)
                    .manageLoading(activity.get()!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(::mapPokemonDetails)
                        .subscribe({ model ->
                            pokemonDetailsLiveData.value = model
                        }, {
                            errorLiveData.value = it
                            Log.e(TAG, null, it)
                        })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}