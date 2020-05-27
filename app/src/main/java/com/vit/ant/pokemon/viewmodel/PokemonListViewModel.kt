package com.vit.ant.pokemon.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonModel
import com.vit.ant.pokemon.network.map.mapPokemon
import com.vit.ant.pokemon.repository.PokemonRepository
import com.vit.ant.pokemon.tools.SingleEvent
import com.vit.ant.pokemon.tools.manageLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Created by Vitiello Antonio
 */
class PokemonListViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    var pokemonsLiveData = MutableLiveData<List<PokemonModel>>()
    var errorLiveData: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    var reachedLimitLiveData: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()

    companion object {
        const val TAG = "HomeViewModel"
        const val TOTAL_ITEMS = 800//items from 807 to 964 have no images
        const val PAGE_LIMIT = 100 //number of items per page => 8 pages + 7 items
    }

    fun nextPokemonsPage(mPokemons: List<PokemonModel>, weakActivity: WeakReference<FragmentActivity>) {
        getPokemons(mPokemons.size, PAGE_LIMIT, mPokemons, weakActivity)
    }

    fun getPokemons(
        offset: Int = 0, pageLimit: Int = PAGE_LIMIT, mPokemons: List<PokemonModel>? = null,
        weakActivity: WeakReference<FragmentActivity>
    ) {
        val limit = if (offset < TOTAL_ITEMS) pageLimit else
            if (offset == TOTAL_ITEMS) 7 else 0
        if (limit == 0) {
            reachedLimitLiveData.postValue(SingleEvent(true))
            return
        }
        compositeDisposable.add(
            PokemonRepository.getPokemons(offset, limit)
                .manageLoading(weakActivity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(::mapPokemon)
                .map { newItems ->
                    mPokemons?.toMutableList()?.apply {
                        addAll(newItems)
                    } ?: newItems
                }
                .subscribe({ models ->
                    pokemonsLiveData.value = models
                }, {
                    val message = getApplication<Application>().getString(R.string.generic_network_error_message)
                    errorLiveData.value = SingleEvent(message)
                    Log.e(TAG, null, it)
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}