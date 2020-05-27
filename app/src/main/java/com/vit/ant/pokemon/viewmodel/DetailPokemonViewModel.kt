package com.vit.ant.pokemon.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vit.ant.pokemon.PokemonApplication
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonDetailsModel
import com.vit.ant.pokemon.model.PokemonModel
import com.vit.ant.pokemon.network.map.mapPokemonDetails
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
class DetailPokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    var pokemonDetailsLiveData = MutableLiveData<PokemonDetailsModel>()
    var errorLiveData: MutableLiveData<SingleEvent<String>> = MutableLiveData()

    companion object {
        private const val TAG = "DetailPokemonViewModel"
    }


    fun getPokemonDetails(id: Int, weakActivity: WeakReference<FragmentActivity>) {
        compositeDisposable.add(
                PokemonRepository.getPokemonDetails(id)
                    .manageLoading(weakActivity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(::mapPokemonDetails)
                        .subscribe({ model ->
                            pokemonDetailsLiveData.value = model
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