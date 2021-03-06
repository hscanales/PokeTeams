package us.vslt.poketeam.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_team_editor.*
import us.vslt.poketeam.R
import us.vslt.poketeam.adapters.teamDetailOnClickListener
import us.vslt.poketeam.adapters.teamDetalAdapter
import us.vslt.poketeam.data.Model.Pokemon
import us.vslt.poketeam.data.Model.PokemonDataRegion
import us.vslt.poketeam.data.Model.User
import us.vslt.poketeam.data.Model.team
import us.vslt.poketeam.data.ViewModel.teamDetalVM

class team_editor : AppCompatActivity(), teamDetailOnClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var userVM: teamDetalVM
    lateinit var adapter: teamDetalAdapter
    lateinit var teamid: String
    lateinit var regionName: String
    lateinit var teamName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_editor)

        teamid = intent.getStringExtra("teamID").toString()
        regionName = intent.getStringExtra("regionName").toString()
        teamName = intent.getStringExtra("teamName").toString()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
        team_editor_name.setText(teamName)
        binder(teamid, regionName, teamName)
    }

    private fun binder(teamid: String, regionName: String, teamNames: String) {

        val fab: View = findViewById(R.id.fab_new_pokemon)
        fab.setOnClickListener {
            val ref = database.child(auth.currentUser!!.uid)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var user = snapshot.getValue(User::class.java)
                    val newpokemon = PokemonDataRegion("ditto")
                    user?.teams?.forEach {
                        if (it.teamID == teamid) {
                            if (it.members!!.size >= 6) {
                                Toast.makeText(
                                    this@team_editor,
                                    "You can only Add 6 Pokemons to the Team",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
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
                        if (it.region_name.toString() == regionName) {
                            if (it.teamID.toString() == teamid) {

                                userVM.getPokemons(it.members!!)

                            }
                        }

                    }
                    userVM.todos().observe(this@team_editor) {
                        adapter.updateList(it)

                    }

                    adapter = teamDetalAdapter(pokemons, this@team_editor, teamNames)

                    members_recycler?.adapter = adapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
        save_team_btn.setOnClickListener {
            var members = mutableListOf<PokemonDataRegion>()
            adapter.pokemons.forEach {
                val poke = PokemonDataRegion(it.name)
                members.add(poke)
            }
            var teamname = team_editor_name.text.toString()
            var update_team = team(teamname, regionName, members, teamid)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user = snapshot.getValue(User::class.java)
                    user?.teams?.forEach {
                        if (it.teamID == update_team.teamID) {
                            it.apply {
                                it.members = update_team.members
                                it.name = update_team.name
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
        delete_team_btn.setOnClickListener {

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user = snapshot.getValue(User::class.java)
                    user?.teams?.forEach {
                        if (it.teamID == teamid) {
                            it.active = false
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

    override fun onItemClicked(poke: Pokemon) {

        val intent = Intent(this, change_pokemon::class.java)
        intent.putExtra("teamid", teamid)
        intent.putExtra("pokemon_to_change", poke.uuid)
        intent.putExtra("current_team", Gson().toJson(adapter.pokemons))
        intent.putExtra("region", regionName)
        startActivity(intent)

    }
}
