package com.vit.ant.pokemon.view.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonModel
import kotlinx.android.synthetic.main.home_list_item.view.*


/**
 * Created by Vitiello Antonio
 */
class PokemonListAdapter(private val listener: (Int) -> Unit) : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {
    val mPokemons = mutableListOf<PokemonModel>()
    private val diffUtilCallback = object : DiffUtil.Callback() {
        lateinit var mNewPokemons: List<PokemonModel>

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mPokemons[oldItemPosition].id == mNewPokemons[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return mPokemons.size
        }

        override fun getNewListSize(): Int {
            return mNewPokemons.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mPokemons[oldItemPosition].name == mNewPokemons[newItemPosition].name
        }
    }

    companion object {
        const val TAG = "PokemonListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = mPokemons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindItem(mPokemons[position])

    fun switchData(data: List<PokemonModel>?) {
        data?.let {
            diffUtilCallback.mNewPokemons = it
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            mPokemons.clear()
            mPokemons.addAll(it)
            diffResult.dispatchUpdatesTo(this@PokemonListAdapter)
        } ?: kotlin.run {
            mPokemons.clear()
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(pokemonModel: PokemonModel) {

            with(itemView) {
                pokemonName.text = pokemonModel.name
                Log.d(TAG, "Start loading image: ${pokemonModel.imageUrl}")
                Picasso.get()
                    .load(pokemonModel.imageUrl)
                    .placeholder(R.drawable.pokeball)
                    .error(R.drawable.pokeball)
                    .into(pokemonImage)

                setOnClickListener {
                    listener.invoke(pokemonModel.id)
                }
            }
        }
    }

}