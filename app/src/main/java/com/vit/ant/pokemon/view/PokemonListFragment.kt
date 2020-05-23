package com.vit.ant.pokemon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.tools.addToStackFragment
import com.vit.ant.pokemon.view.adapter.PokemonListAdapter
import com.vit.ant.pokemon.viewmodel.PokemonListViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.ref.WeakReference


/**
 * Created by Vitiello Antonio
 */
class PokemonListFragment : Fragment() {

    private lateinit var mViewModel: PokemonListViewModel
    private lateinit var mAdapter: PokemonListAdapter

    companion object {
        const val TAG = "PokemonListFragment"

        fun newInstance() = PokemonListFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(PokemonListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.pokemonsLiveData.observe(viewLifecycleOwner, Observer { models ->
            mAdapter.switchData(models)
        })

        mViewModel.getPokemons(WeakReference(requireActivity()))

        initComponents()
    }

    private fun initComponents() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        pokemonRecyclerView.layoutManager = gridLayoutManager
        mAdapter = PokemonListAdapter { id ->
            addToStackFragment(
                R.id.fragmentContainer, PokemonDetailsFragment.newInstance(id),
                PokemonDetailsFragment.TAG
            )
        }
        pokemonRecyclerView.adapter = mAdapter
    }

}
