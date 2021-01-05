package com.vit.ant.pokemon.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonDetailsModel
import com.vit.ant.pokemon.network.map.mapPokemonDetails
import com.vit.ant.pokemon.repository.PokemonRepository
import com.vit.ant.pokemon.tools.SingleEvent
import com.vit.ant.pokemon.tools.manageLoading
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Vitiello Antonio
 */
@FragmentScoped
class DetailPokemonViewModel @Inject constructor(private val context: Context, private val repository: PokemonRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var pokemonDetailsLiveData = MutableLiveData<PokemonDetailsModel>()
    var errorLiveData: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    var progressWheelLiveData: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()

    companion object {
        private const val TAG = "DetailPokemonViewModel"
    }

    fun getPokemonDetails(id: Int) {
        compositeDisposable.add(repository.getPokemonDetails(id).manageLoading(progressWheelLiveData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map(::mapPokemonDetails).subscribe({ model ->
                pokemonDetailsLiveData.value = model
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