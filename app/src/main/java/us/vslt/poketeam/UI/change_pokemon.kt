package us.vslt.poketeam.UI

import android.graphics.Region
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_change_pokemon.*
import us.vslt.poketeam.R
import us.vslt.poketeam.adapters.OnItemClickListenerRegion
import us.vslt.poketeam.adapters.RegionExtendedAdapter
import us.vslt.poketeam.data.Model.*
import us.vslt.poketeam.data.ViewModel.RegionExtendedViewModel
import java.lang.reflect.Type

class change_pokemon : AppCompatActivity(), OnItemClickListenerRegion{

    lateinit var RegionExtendedVM : RegionExtendedViewModel
    lateinit var  RegionExtendedAdapter : RegionExtendedAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var  teamid : String
    lateinit var teamActual : MutableList<Pokemon>
    lateinit var pkToChange : String
    lateinit var regionName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pokemon)

        var data = intent.getStringExtra("current_team")
        teamid = intent.getStringExtra("teamid")!!
        pkToChange = intent.getStringExtra("pokemon_to_change")!!
        regionName = intent.getStringExtra("region")!!
        var listType: Type? = object : TypeToken<List<Pokemon?>?>() {}.type
        teamActual = Gson().fromJson(data,listType)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
        binder(regionName!!)

    }

    fun binder(regionNames : String){
        RegionExtendedVM = ViewModelProviders.of(this).get(RegionExtendedViewModel::class.java)
        RegionExtendedVM.getPokemonsByRegion(regionNames)
        var lista = ArrayList<Pokemon_Regional>()
        RegionExtendedAdapter = RegionExtendedAdapter(lista,this)

        region_pokemon_rv.apply {
            adapter = RegionExtendedAdapter
            layoutManager = LinearLayoutManager(this@change_pokemon)
        }


        RegionExtendedVM.todos().observe(this, Observer {
            RegionExtendedAdapter.updateList(it)
        })

        RegionExtendedVM.getPokemonsByRegion(regionName)
    }

    override fun onItemClicked(region: Pokemon_Regional, view : View) {
        val ref = database.child(auth.currentUser!!.uid)
        var miembros : MutableList<PokemonDataRegion> = mutableListOf()
        teamActual.forEach(){
            if(it.uuid==pkToChange){
                it.name = region.name
            }
            miembros.add(PokemonDataRegion(it.name))
        }

        var newteam = team("",regionName,miembros,teamid,true)
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)
                user?.teams?.forEach(){
                    if(teamid == it.teamID){
                        it.apply{
                          this.members = newteam.members
                        }
                    }
                }
                ref.setValue(user)
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}

