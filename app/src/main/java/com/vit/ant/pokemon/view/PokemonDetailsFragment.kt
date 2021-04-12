package com.vit.ant.pokemon.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonDetailsModel
import com.vit.ant.pokemon.tools.SingleEvent
import com.vit.ant.pokemon.tools.Utils
import com.vit.ant.pokemon.view.adapter.PokemonStatsAdapter
import com.vit.ant.pokemon.view.adapter.PokemonTypeAdapter
import com.vit.ant.pokemon.view.widget.FloatingToastDialog
import com.vit.ant.pokemon.viewmodel.DetailPokemonViewModel
import kotlinx.android.synthetic.main.fragment_pokemon_details.*

/**
 * Created by Vitiello Antonio
 */
class PokemonDetailsFragment : Fragment() {

    //private lateinit var mViewModel: DetailPokemonViewModel
    private val mViewModel by viewModels<DetailPokemonViewModel>()

    private lateinit var mTypeAdapter: PokemonTypeAdapter
    private lateinit var mStatsAdapter: PokemonStatsAdapter
    private var mPokemonId = 0

    companion object {
        const val TAG = "PokemonDetailsFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move) //for Shared Element Transition

//        mViewModel = ViewModelProvider(this).get(DetailPokemonViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pokemon_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.pokemonDetailsLiveData.observe(viewLifecycleOwner, ::fillPokemonDetails)
        mViewModel.errorLiveData.observe(viewLifecycleOwner, ::showErrorDialog)
        mViewModel.progressWheelLiveData.observe(viewLifecycleOwner, ::showProgressWheel)

        mPokemonId = PokemonDetailsFragmentArgs.fromBundle(requireArguments()).id //Navigation: pass safe args
        mViewModel.getPokemonDetails(mPokemonId)

        initComponents()
    }

    private fun initComponents() {
        pokemonImage.transitionName = mPokemonId.toString(10)

        //typesRecyclerView.layoutManager = StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL)
        mTypeAdapter = PokemonTypeAdapter()
        typesRecyclerView.adapter = mTypeAdapter

        //statsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL)
        mStatsAdapter = PokemonStatsAdapter()
        statsRecyclerView.adapter = mStatsAdapter
    }

    private fun fillPokemonDetails(detailsModel: PokemonDetailsModel) {
        loadImage(detailsModel.imageUrl)
        pokemonName.text = detailsModel.name
        val heightString = Utils.oneDecimalFormater.format(detailsModel.height ?: 0.0)
        pokemonHeight.text = getString(R.string.pokemon_height, heightString)
        val weightString = Utils.oneDecimalFormater.format(detailsModel.weight ?: 0.0)
        pokemonWeight.text = getString(R.string.pokemon_weight, weightString)
        mTypeAdapter.switchData(detailsModel.types)
        mStatsAdapter.switchData(detailsModel.stats)
    }

    private fun loadImage(imageUrl: String) {
        Picasso.get()
            .load(imageUrl)
            .fit()
            .placeholder(R.drawable.pokeball)
            .error(R.drawable.pokeball)
            .into(pokemonImage, object : Callback {
                override fun onSuccess() {
                    Log.d(TAG, "Image loaded: $imageUrl")
                }

                override fun onError(exc: Exception) {
                    Log.e(TAG, "Error while loading image: $imageUrl", exc)
                }
            })
    }

    private fun showProgressWheel(event: SingleEvent<Boolean>) {
        progressView.isVisible = event.getContentIfNotHandled() == true
    }

    private fun showErrorDialog(event: SingleEvent<String>) {
        event.getContentIfNotHandled()?.let { message ->
            FloatingToastDialog(requireContext(), message, FloatingToastDialog.FloatingToastType.Error)
                .fade()
                .show()
        }
    }

}
