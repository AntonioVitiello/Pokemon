package com.vit.ant.pokemon.viewmodel

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vit.ant.pokemon.model.PokemonModel
import com.vit.ant.pokemon.network.map.mapPokemon
import com.vit.ant.pokemon.repository.PokemonRepository
import com.vit.ant.pokemon.tools.manageLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Created by Vitiello Antonio
 */
class PokemonListViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var pokemonsLiveData = MutableLiveData<List<PokemonModel>>()
    var errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    companion object {
        private const val TAG = "HomeViewModel"
    }


    fun getPokemons(activity: WeakReference<FragmentActivity>) {
        compositeDisposable.add(
            PokemonRepository.getPokemons(0, 964)
                .manageLoading(activity.get()!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(::mapPokemon)
                .subscribe({ models ->
                    pokemonsLiveData.value = models
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