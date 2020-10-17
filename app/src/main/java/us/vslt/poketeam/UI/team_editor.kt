package us.vslt.poketeam.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_team_editor.*
import kotlinx.android.synthetic.main.activity_team_manager.*
import us.vslt.poketeam.R
import us.vslt.poketeam.adapters.teamAdapter
import us.vslt.poketeam.adapters.teamDetailOnClickListener
import us.vslt.poketeam.adapters.teamDetalAdapter
import us.vslt.poketeam.data.Model.*
import us.vslt.poketeam.data.ViewModel.teamDetalVM
import us.vslt.poketeam.data.ViewModel.teamViewModel
import java.util.*
import kotlin.collections.ArrayList

class team_editor : AppCompatActivity() , teamDetailOnClickListener{

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var userVM: teamDetalVM
    lateinit var adapter: teamDetalAdapter
    lateinit var  teamid : String
    lateinit var regionName : String
    lateinit var teamName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_editor)

        teamid = intent.getStringExtra("teamID").toString()
        regionName = intent.getStringExtra("regionName").toString()
        teamName = intent.getStringExtra("teamName").toString()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
        team_editor_name.setText(teamName)
        binder(teamid,regionName,teamName)
    }

    private fun binder(teamid: String, regionName: String,teamNames : String) {



        val fab : View = findViewById(R.id.fab_new_pokemon)
        fab.setOnClickListener(){
            val ref = database.child(auth.currentUser!!.uid)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var user = snapshot.getValue(User::class.java)
                    val newpokemon = PokemonDataRegion("ditto")
                    user?.teams?.forEach() {
                        if (it.teamID == teamid) {
                            if (it.members!!.size >= 6) {
                                Toast.makeText(this@team_editor, "You can only Add 6 Pokemons to the Team", Toast.LENGTH_SHORT).show()
                            }else{
                                it.members!!.add(newpokemon)
                            }
                        }
                    }
                    database.child(auth.currentUser!!.uid).setValue(user)
                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


        var ref = database.child(auth.currentUser!!.uid)
        var pokemons = ArrayList<Pokemon>()
        members_recycler?.layoutManager = LinearLayoutManager(this)
        userVM = ViewModelProviders.of(this@team_editor).get(teamDetalVM::class.java)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    pokemons.clear()
                    user?.teams?.forEach {
                        if (it.region_name.toString() == regionName){
                            if(it.teamID.toString() == teamid){

                                userVM.getPokemons(it.members!!)

                            }
                        }

                    }
                    userVM.todos().observe(this@team_editor){
                        adapter.updateList(it)

                    }

                    adapter = teamDetalAdapter(pokemons, this@team_editor,teamNames)

                    members_recycler?.adapter = adapter



                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error",error.toString())
            }
        })}

    override fun onItemClicked(poke: Pokemon) {
        TODO("Not yet implemented")
    }
}
