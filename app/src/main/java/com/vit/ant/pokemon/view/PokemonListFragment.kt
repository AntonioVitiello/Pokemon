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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.view.adapter.PokemonListAdapter
import com.vit.ant.pokemon.view.widget.FloatingToastDialog
import com.vit.ant.pokemon.view.widget.FloatingToastDialog.FloatingToastType
import com.vit.ant.pokemon.viewmodel.PokemonListViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.ref.WeakReference


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
        mViewModel.getPokemons(weakActivity = WeakReference(requireActivity()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showWelcomeMessage()

        mViewModel.pokemonsLiveData.observe(viewLifecycleOwner, Observer { models ->
            swipeToRefreshLayout.isRefreshing = false
            mIsPagingEnabled = true
            mAdapter.switchData(models)
        })
        mViewModel.errorLiveData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { message ->
                FloatingToastDialog(requireContext(), message, FloatingToastType.Error).fade().show()
            }
        })

        initComponents()
    }

    private fun showWelcomeMessage() {
        mViewModel.pokemonsLiveData.value ?: run {
            FloatingToastDialog(requireContext(),
                                R.string.app_name,
                                R.string.welcome_message,
                                FloatingToastType.Alert).timer(FLOATING_TOAST_TIMOUT).show()
        }
    }

    private fun initComponents() {
        val navController = Navigation.findNavController(requireView())
        val layoutManager = GridLayoutManager(requireContext(), 2)
        pokemonRecyclerView.layoutManager = layoutManager
        mAdapter = PokemonListAdapter { id ->
            navController.navigate(PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id))
            //            navController.navigate(R.id.action_pokemonListFragment_to_pokemonDetailsFragment)
        }
        pokemonRecyclerView.adapter = mAdapter

        //Paging
        pokemonRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (mAdapter.itemCount > PokemonListViewModel.TOTAL_ITEMS && !recyclerView.canScrollVertically(1)) {
                    FloatingToastDialog(requireContext(),
                                        getString(R.string.end_of_list_message),
                                        FloatingToastType.Warning).timer(FLOATING_TOAST_TIMOUT).show()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (mIsPagingEnabled) {
                        mVisibleItems = layoutManager.childCount
                        mTotalItems = layoutManager.itemCount
                        mLastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                        if (mVisibleItems + mLastVisiblesItems >= mTotalItems) {
                            mIsPagingEnabled = false
                            mViewModel.nextPokemonsPage(mAdapter.mPokemons, WeakReference(requireActivity()))
                            Log.d(TAG, "Loading pokemons page: visibleItems=$mVisibleItems, lastVisiblesItems=$mLastVisiblesItems, totalItems=$mTotalItems")
                        }
                    }
                }
            }
        })

        swipeToRefreshLayout.setOnRefreshListener {
            if (mIsPagingEnabled && mAdapter.itemCount < PokemonListViewModel.TOTAL_ITEMS) {
                mIsPagingEnabled = false
                mViewModel.nextPokemonsPage(mAdapter.mPokemons, WeakReference(requireActivity()))
            } else {
                swipeToRefreshLayout.isRefreshing = false
                FloatingToastDialog(requireContext(),
                                    getString(R.string.end_of_list_message),
                                    FloatingToastType.Warning).timer(FLOATING_TOAST_TIMOUT).show()
            }
        }
    }

}
