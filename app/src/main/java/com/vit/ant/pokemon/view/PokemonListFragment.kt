package com.vit.ant.pokemon.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonModel
import com.vit.ant.pokemon.tools.SingleEvent
import com.vit.ant.pokemon.view.adapter.PokemonListAdapter
import com.vit.ant.pokemon.view.widget.FloatingToastDialog
import com.vit.ant.pokemon.view.widget.FloatingToastDialog.FloatingToastType
import com.vit.ant.pokemon.viewmodel.PokemonListViewModel
import kotlinx.android.synthetic.main.fragment_pokemon_list.*


/**
 * Created by Vitiello Antonio
 */
class PokemonListFragment : Fragment() {

    private lateinit var mViewModel: PokemonListViewModel
    private lateinit var mAdapter: PokemonListAdapter
    private var mIsPagingEnabled = true
    private var mLastVisiblesItems = 0
    private var mVisibleItems = 0
    private var mTotalItems = 0

    companion object {
        const val TAG = "PokemonListFragment"
        const val FLOATING_TOAST_TIMOUT = 4000L
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(PokemonListViewModel::class.java)
        mViewModel.getPokemons()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showWelcomeMessage()

        mViewModel.pokemonsLiveData.observe(viewLifecycleOwner, Observer { fillPokemonList(it) })
        mViewModel.progressWheelLiveData.observe(
            viewLifecycleOwner,
            Observer { showProgressWheel(it) })
        mViewModel.reachedLimitLiveData.observe(
            viewLifecycleOwner,
            Observer { showEndOfListDialog(it) })
        mViewModel.errorLiveData.observe(viewLifecycleOwner, Observer { showErrorDialog(it) })

        initComponents()
    }

    private fun initComponents() {
        val navController = Navigation.findNavController(requireView())
        val layoutManager = GridLayoutManager(requireContext(), 2)
        pokemonRecyclerView.layoutManager = layoutManager
        mAdapter = PokemonListAdapter { id, imageView ->

            val extras = FragmentNavigatorExtras(imageView to id.toString(10))
            val action =
                PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id)
            navController.navigate(action, extras)

//            navController.navigate(R.id.action_pokemonListFragment_to_pokemonDetailsFragment)
//            navController.navigate(PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id))
        }
        pokemonRecyclerView.adapter = mAdapter

        //Paging
        pokemonRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                //do nothing
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (mIsPagingEnabled) {
                        mVisibleItems = layoutManager.childCount
                        mTotalItems = layoutManager.itemCount
                        mLastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                        if (mVisibleItems * 2 + mLastVisiblesItems >= mTotalItems) {
                            mIsPagingEnabled = false
                            mViewModel.nextPokemonsPage(mAdapter.mPokemons)
                            Log.d(
                                TAG,
                                "Loading pokemons page: visibleItems=$mVisibleItems, lastVisiblesItems=$mLastVisiblesItems, totalItems=$mTotalItems"
                            )
                        }
                    }
                }
            }
        })

        swipeToRefreshLayout.setOnRefreshListener {
            mIsPagingEnabled = false
            mViewModel.refreshPokemonList(mAdapter.itemCount)
        }
    }

    private fun fillPokemonList(models: List<PokemonModel>?) {
        swipeToRefreshLayout.isRefreshing = false
        mIsPagingEnabled = true
        mAdapter.switchData(models)
    }

    private fun showWelcomeMessage() {
        mViewModel.pokemonsLiveData.value ?: run {
            FloatingToastDialog(
                requireContext(),
                R.string.app_name,
                R.string.welcome_message,
                FloatingToastType.Alert
            )
                .timer(FLOATING_TOAST_TIMOUT)
                .show()
        }
    }

    private fun showProgressWheel(event: SingleEvent<Boolean>) {
        progressView.visibility =
            if (event.getContentIfNotHandled() == true) View.VISIBLE else View.GONE
    }

    private fun showEndOfListDialog(event: SingleEvent<Boolean>) {
        if (event.getContentIfNotHandled() == true) {
            FloatingToastDialog(
                requireContext(),
                getString(R.string.end_of_list_message),
                FloatingToastType.Warning
            )
                .timer(FLOATING_TOAST_TIMOUT)
                .show()
        }
    }

    private fun showErrorDialog(event: SingleEvent<String>) {
        event.getContentIfNotHandled()?.let { message ->
            FloatingToastDialog(requireContext(), message, FloatingToastType.Error)
                .fade()
                .show()
        }
    }

}
