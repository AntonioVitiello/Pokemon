package com.vit.ant.pokemon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonDetailsModel
import com.vit.ant.pokemon.tools.Utils
import com.vit.ant.pokemon.view.adapter.PokemonStatsAdapter
import com.vit.ant.pokemon.view.adapter.PokemonTypeAdapter
import com.vit.ant.pokemon.viewmodel.DetailPokemonViewModel
import kotlinx.android.synthetic.main.fragment_pokemon_details.*
import java.lang.ref.WeakReference

/**
 * Created by Vitiello Antonio
 */
class PokemonDetailsFragment : Fragment() {
    private lateinit var mViewModel: DetailPokemonViewModel
    private lateinit var mTypeAdapter: PokemonTypeAdapter
    private lateinit var mStatsAdapter: PokemonStatsAdapter
    private var mPokemonId = 0

    companion object {
        const val TAG = "PokemonDetailsFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(DetailPokemonViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_pokemon_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.pokemonDetailsLiveData.observe(viewLifecycleOwner, Observer { model ->
            fillPokemonDetails(model)
        })

        mPokemonId = PokemonDetailsFragmentArgs.fromBundle(requireArguments()).id
        mViewModel.getPokemonDetails(mPokemonId, WeakReference(requireActivity()))

        initComponents()
    }

    private fun initComponents() {
        typesRecyclerView.layoutManager = StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL)
        mTypeAdapter = PokemonTypeAdapter()
        typesRecyclerView.adapter = mTypeAdapter

        statsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL)
        mStatsAdapter = PokemonStatsAdapter()
        statsRecyclerView.adapter = mStatsAdapter
    }

    private fun fillPokemonDetails(detailsModel: PokemonDetailsModel) {
        Picasso.get()
            .load(detailsModel.imageUrl)
            .placeholder(R.drawable.pokeball)
            .error(R.drawable.pokeball)
            .into(pokemonImage)
        pokemonName.text = detailsModel.name
        val heightString = Utils.oneDecimalFormater.format(detailsModel.height ?: 0.0)
        pokemonHeight.text = getString(R.string.pokemon_height, heightString)
        val weightString = Utils.oneDecimalFormater.format(detailsModel.weight ?: 0.0)
        pokemonWeight.text = getString(R.string.pokemon_weight, weightString)
        mTypeAdapter.switchData(detailsModel.types)
        mStatsAdapter.switchData(detailsModel.stats)
    }

}
