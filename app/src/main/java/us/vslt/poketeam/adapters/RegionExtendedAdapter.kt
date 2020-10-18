package us.vslt.poketeam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pokemon_card.view.*
import us.vslt.poketeam.R
import us.vslt.poketeam.data.Model.Pokemon_Regional

class RegionExtendedAdapter(
    var regiones: List<Pokemon_Regional>,
    val itemClickListener: OnItemClickListenerRegion
) : RecyclerView.Adapter<RegionExtendedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(poke: Pokemon_Regional, itemClickListener: OnItemClickListenerRegion) =
            with(itemView) {
                this.poke_name.text = poke.name
                this.poke_number.text = poke.id.toString()

                Glide.with(this).load(poke.sprites.front_default).override(60, 60).into(imageView3)
                this.setOnClickListener {
                    itemClickListener.onItemClicked(poke, it)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(regiones[position], itemClickListener)
    }

    fun updateList(newRegion: List<Pokemon_Regional>) {
        this.regiones = newRegion
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = regiones.size


}

interface OnItemClickListenerRegion {
    fun onItemClicked(region: Pokemon_Regional, view: View)
}