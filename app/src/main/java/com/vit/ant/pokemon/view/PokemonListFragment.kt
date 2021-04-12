package com.vit.ant.pokemon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import javax.inject.Inject


/**
 * Created by Vitiello Antonio
 */
@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    @Inject
    lateinit var mViewModel: PokemonListViewModel
    private lateinit var mAdapter: PokemonListAdapter
    private var mIsPagingEnabled = true
    private var showWelcomeMessage = true

    companion object {
        const val TAG = "PokemonListFragment"
        const val FLOATING_TOAST_TIMOUT = 4000L
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.getPokemons()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.pokemonsLiveData.observe(viewLifecycleOwner, Observer(this::addToPokemonList))
        mViewModel.refreshLiveData.observe(viewLifecycleOwner, Observer(this::refreshPokemonList))
        mViewModel.progressWheelLiveData.observe(viewLifecycleOwner, { showProgressWheel(it) })
        mViewModel.reachedLimitLiveData.observe(viewLifecycleOwner, { showEndOfListDialog(it) })
        mViewModel.errorLiveData.observe(viewLifecycleOwner, Observer(this::showErrorDialog))

        initComponents()
    }

    override fun onResume() {
        super.onResume()
        if (showWelcomeMessage) {
            showWelcomeMessage = false
            showWelcomeMessage()
        }
    }

    private fun initComponents() {
        val navController = Navigation.findNavController(requireView())
//        val layoutManager = GridLayoutManager(requireContext(), 2)
//        pokemonRecyclerView.layoutManager = layoutManager
        val layoutManager = pokemonRecyclerView.layoutManager as GridLayoutManager
        mAdapter = PokemonListAdapter { id, imageView ->

            val extras = FragmentNavigatorExtras(imageView to id.toString(10)) //Navigation: for Shared Element Transition
            val action =
                PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id) //Navigation: pass safe args
            navController.navigate(action, extras)

//            navController.navigate(R.id.action_pokemonListFragment_to_pokemonDetailsFragment)
//            navController.navigate(PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id))
        }
        pokemonRecyclerView.setHasFixedSize(true)
        pokemonRecyclerView.adapter = mAdapter

        //Paging
        pokemonRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                //do nothing
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (mIsPagingEnabled) {
                        val visibleItemsCount = layoutManager.childCount
                        val itemsPerPage = layoutManager.itemCount
                        val prefetchLimit = itemsPerPage - (2 * visibleItemsCount) - 1
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                        if (lastVisibleItemPosition >= prefetchLimit) {
                            mIsPagingEnabled = false
                            mViewModel.nextPokemonsPage(mAdapter.itemCount)
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

    private fun addToPokemonList(models: List<PokemonModel>?) {
        swipeToRefreshLayout.isRefreshing = false
        mIsPagingEnabled = true
        mAdapter.switchWithAddedData(models)
    }

    private fun refreshPokemonList(models: List<PokemonModel>?) {
        swipeToRefreshLayout.isRefreshing = false
        mIsPagingEnabled = true
        mAdapter.updateData(models)
    }

    private fun showWelcomeMessage() {
        FloatingToastDialog(requireContext(), R.string.app_name, R.string.welcome_message, FloatingToastType.Alert)
            .timer(FLOATING_TOAST_TIMOUT)
            .show()
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
