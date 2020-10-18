package us.vslt.poketeam.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.region_card.view.*
import us.vslt.poketeam.R
import us.vslt.poketeam.data.Model.region

class RegionAdapter(var regiones: List<region>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RegionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(region: region, itemClickListener: OnItemClickListener) = with(itemView) {
            this.region_name.text = region.region_name
            this.setOnClickListener({
                itemClickListener.onItemClicked(region)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.region_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(regiones[position], itemClickListener)
    }

    fun updateList(newRegion: List<region>) {
        this.regiones = newRegion
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = regiones.size

}

interface OnItemClickListener {
    fun onItemClicked(region: region)
}