package com.vit.ant.pokemon.viewmodel

import android.app.Application
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
        const val TOTAL_ITEMS = 964//total items
        const val PAGE_LIMIT = 100 //number of items per page => 9 pages
    }

    fun nextPokemonsPage(mPokemons: List<PokemonModel>, weakActivity: WeakReference<FragmentActivity>) {
        getPokemons(mPokemons.size, PAGE_LIMIT, mPokemons, weakActivity)
    }

    fun getPokemons(
        offset: Int = 0, pageSize: Int = PAGE_LIMIT, mPokemons: List<PokemonModel>? = null,
        weakActivity: WeakReference<FragmentActivity>
    ) {
        if (offset >= TOTAL_ITEMS) {
            reachedLimitLiveData.postValue(SingleEvent(true))
            return
        }
        compositeDisposable.add(
            PokemonRepository.getPokemons(offset, pageSize)
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