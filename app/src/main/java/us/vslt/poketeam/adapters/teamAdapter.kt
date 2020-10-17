package us.vslt.poketeam.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.team_card.view.*
import us.vslt.poketeam.R
import us.vslt.poketeam.data.Model.region
import us.vslt.poketeam.data.Model.team

class teamAdapter( var teams : List<team>,val listener: teamOnClickListener) : RecyclerView.Adapter<teamAdapter.VH>(){

    class VH(itemView : View):RecyclerView.ViewHolder(itemView){
        fun bind(team: team,listener: teamOnClickListener)= with(itemView){
            this.team_name.text = team.name
            this.setOnClickListener {

                listener.onItemClicked(team)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.team_card,parent,false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(teams[position],listener)
    }

    override fun getItemCount(): Int = teams.size


    }


    interface teamOnClickListener{
        fun onItemClicked(team: team)
    }
