package com.vit.ant.pokemon.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonModel
import com.vit.ant.pokemon.network.map.mapPokemon
import com.vit.ant.pokemon.repository.PokemonRepository
import com.vit.ant.pokemon.tools.SingleEvent
import com.vit.ant.pokemon.tools.manageLoading
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Vitiello Antonio
 */
@ActivityScoped
class PokemonListViewModel @Inject constructor(private val context: Context, private val repository: PokemonRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var pokemonsLiveData = MutableLiveData<List<PokemonModel>>()
    var refreshLiveData = MutableLiveData<List<PokemonModel>>()
    var errorLiveData: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    var progressWheelLiveData: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
    var reachedLimitLiveData: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()

    companion object {
        const val TAG = "HomeViewModel"
        const val TOTAL_ITEMS = 964//total items
        const val PAGE_LIMIT = 100 //number of items per page => 9 pages
    }

    fun nextPokemonsPage(currentPokemonsCount: Int) {
        getPokemons(currentPokemonsCount, PAGE_LIMIT)
    }

    fun getPokemons(offset: Int = 0, pageSize: Int = PAGE_LIMIT) {
        if (offset >= TOTAL_ITEMS) {
            reachedLimitLiveData.postValue(SingleEvent(true))
            return
        }
        compositeDisposable.add(repository.getPokemons(offset, pageSize).manageLoading(progressWheelLiveData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map(::mapPokemon).map { newItems ->
                pokemonsLiveData.value?.toMutableList()?.apply {
                    addAll(newItems)
                } ?: newItems
            }.subscribe({ models ->
                pokemonsLiveData.value = models
            }, {
                val message = context.getString(R.string.generic_network_error_message)
                errorLiveData.value = SingleEvent(message)
                Log.e(TAG, null, it)
            }))
    }

    fun refreshPokemonList(listSize: Int) {
        compositeDisposable.add(repository.getPokemons(0, listSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .map(::mapPokemon).subscribe({ models ->
                refreshLiveData.value = models
            }, {
                val message = context.getString(R.string.generic_network_error_message)
                errorLiveData.value = SingleEvent(message)
                Log.e(TAG, null, it)
            }))
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}