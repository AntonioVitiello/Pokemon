package com.vit.ant.pokemon.view.adapter


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
    private val mPokemons = mutableListOf<PokemonModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = mPokemons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindItem(mPokemons[position])

    fun switchData(data: List<PokemonModel>?) {
        mPokemons.clear()
        data?.let {
            mPokemons.addAll(data)
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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