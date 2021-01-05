package com.vit.ant.pokemon.view.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vit.ant.pokemon.R
import com.vit.ant.pokemon.model.PokemonModel
import kotlinx.android.synthetic.main.home_list_item.view.*


/**
 * Created by Vitiello Antonio
 */
class PokemonListAdapter(private val listener: (Int, ImageView) -> Unit) :
    RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {
    val mPokemons = mutableListOf<PokemonModel>()
    val mDiffCallback = DiffCallback()

    companion object {
        const val TAG = "PokemonListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = mPokemons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindItem(mPokemons[position])

    fun switchData(data: List<PokemonModel>?) {
        mPokemons.clear()
        if (data != null) {
            mPokemons.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun switchWithAddedData(data: List<PokemonModel>?) {
        if (data == null || mPokemons.isEmpty()) {
            switchData(data)
        } else {
            val prevSize = mPokemons.size
            mPokemons.clear()
            mPokemons.addAll(data)
            notifyItemRangeInserted(prevSize, data.size)
        }
    }

    fun addAll(data: List<PokemonModel>?) {
        if (data == null || mPokemons.isEmpty()) {
            switchData(data)
        } else {
            val prevSize = mPokemons.size
            mPokemons.addAll(data)
            notifyItemRangeInserted(prevSize, data.size)
        }
    }

    fun updateData(data: List<PokemonModel>?) {
        if (data == null || mPokemons.isEmpty()) {
            switchData(data)
        } else {
            mDiffCallback.newData(data)
            val diffResult = DiffUtil.calculateDiff(mDiffCallback)
            mPokemons.clear()
            mPokemons.addAll(data)
            diffResult.dispatchUpdatesTo(this)
        }
    }


    inner class DiffCallback : DiffUtil.Callback() {
        private val newPokemons = mutableListOf<PokemonModel>()

        fun newData(data: List<PokemonModel>) {
            newPokemons.apply {
                clear()
                addAll(data)
            }
        }

        override fun getOldListSize(): Int {
            return mPokemons.size
        }

        override fun getNewListSize(): Int {
            return newPokemons.size
        }

        /**
         * Called by the DiffUtil to decide whether two object represent the same Item.
         * If items have unique ids, this method should check their id equality.
         */
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mPokemons[oldItemPosition].id == newPokemons[newItemPosition].id
        }

        /**
         * Checks whether two items have the same data.
         * Called by DiffUtil only if areItemsTheSame returns true.
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mPokemons[oldItemPosition] == newPokemons[newItemPosition]
        }

        /**
         * If areItemTheSame return true and areContentsTheSame returns false,
         * DiffUtil calls this method to get a payload about the change.
         */
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(pokemonModel: PokemonModel) {

            with(itemView) {
                pokemonName.text = pokemonModel.name
                loadImage(pokemonModel.imageUrl)
                pokemonImage.transitionName = pokemonModel.id.toString(10)
                setOnClickListener {
                    listener.invoke(pokemonModel.id, pokemonImage)
                }
            }
        }

        private fun View.loadImage(imageUrl: String) {
            Picasso.get()
                .load(imageUrl)
                .fit()
                .placeholder(R.drawable.pokeball)
                .error(R.drawable.pokeball)
//                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(pokemonImage, object : Callback {
                    override fun onSuccess() {
                        Log.d(TAG, "Image loaded: $imageUrl")
                    }

                    override fun onError(exc: Exception) {
                        Log.e(TAG, "Error while loading image: $imageUrl", exc)
                    }
                })
        }
    }

}