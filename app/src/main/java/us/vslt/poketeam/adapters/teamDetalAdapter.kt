package us.vslt.poketeam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pokemon_card.view.*
import us.vslt.poketeam.R
import us.vslt.poketeam.data.Model.Pokemon

class teamDetalAdapter(
    var pokemons: List<Pokemon>,
    val listener: teamDetailOnClickListener,
    val teamNames: String
) : RecyclerView.Adapter<teamDetalAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(poke: Pokemon, listener: teamDetailOnClickListener, teamNames: String) =
            with(itemView) {
                this.poke_name.text = poke.name
                this.poke_number.text = poke.id.toString()

                Glide.with(this).load(poke.sprites.front_default).override(60, 60).into(imageView3)
                this.setOnClickListener {
                    listener.onItemClicked(poke)
                }

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_card, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(pokemons[position], listener, teamNames)

    }

    override fun getItemCount(): Int = pokemons.size

    fun updateList(nuevaLista: List<Pokemon>) {
        this.pokemons = nuevaLista
        notifyDataSetChanged()
    }

}


interface teamDetailOnClickListener {
    fun onItemClicked(poke: Pokemon)
}