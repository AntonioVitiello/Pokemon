package com.vit.ant.pokemon.view.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonModel
import kotlinx.android.synthetic.main.home_list_item.view.*


/**
 * Created by Vitiello Antonio
 */
class PokemonListAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {
    private val pokemons = mutableListOf<PokemonModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(itemView, listener)
    }

    override fun getItemCount() = pokemons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindItem(pokemons[position])

    fun switchData(data: List<PokemonModel>?) {
        pokemons.clear()
        data?.let {
            pokemons.addAll(data)
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View, listener: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bindItem(pokemonModel: PokemonModel) {

            with(itemView) {
                pokemonName.text = pokemonModel.name
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