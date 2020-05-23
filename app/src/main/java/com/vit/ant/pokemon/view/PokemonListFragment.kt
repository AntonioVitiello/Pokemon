package com.vit.ant.pokemon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.view.adapter.PokemonListAdapter
import com.vit.ant.pokemon.viewmodel.PokemonListViewModel
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * Created by Vitiello Antonio
 */
class PokemonListFragment : Fragment() {

    private lateinit var mViewModel: PokemonListViewModel
    private lateinit var mAdapter: PokemonListAdapter

    companion object {
        const val TAG = "PokemonListFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(PokemonListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.pokemonsLiveData.observe(viewLifecycleOwner, Observer { models ->
            mAdapter.switchData(models)
        })

        mViewModel.nextPokemonsPage(requireActivity())

        initComponents()
    }

    private fun initComponents() {
        val navController = Navigation.findNavController(requireView())
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        pokemonRecyclerView.layoutManager = gridLayoutManager
        mAdapter = PokemonListAdapter { id ->
            navController.navigate(PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id))
//            navController.navigate(R.id.action_pokemonListFragment_to_pokemonDetailsFragment)
        }
        pokemonRecyclerView.adapter = mAdapter
    }

}
