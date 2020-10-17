package us.vslt.poketeam.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_team_manager.*
import us.vslt.poketeam.R
import us.vslt.poketeam.adapters.teamAdapter
import us.vslt.poketeam.adapters.teamOnClickListener
import us.vslt.poketeam.data.Model.PokemonDataRegion
import us.vslt.poketeam.data.Model.User
import us.vslt.poketeam.data.Model.team
import us.vslt.poketeam.data.ViewModel.teamViewModel
import kotlin.random.Random


class team_manager : AppCompatActivity(), teamOnClickListener {

    lateinit var drawerLayout: DrawerLayout
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var userVM: teamViewModel
    lateinit var adapter: teamAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("users")
        val region_name = intent.getStringExtra("region")
        setContentView(R.layout.activity_team_manager)
        drawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        binder(region_name)

    }


    fun binder(region_name: String?) {
        val fab : View = findViewById(R.id.fab)
        fab.setOnClickListener(){
            val ref = database.child(auth.currentUser!!.uid)
            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                        var user = snapshot.getValue(User::class.java)

                    if(user==null){
                        val newTeam = team(
                            "New Team",
                            region_name,
                            arrayListOf(
                                PokemonDataRegion("ditto"),
                                PokemonDataRegion("ditto"),
                                PokemonDataRegion("ditto")
                            ),
                            java.util.UUID.randomUUID().toString()
                        )
                        val lista = mutableListOf<team>(newTeam)
                        user = User(auth.currentUser!!.uid,lista)
                        database.child(auth.currentUser!!.uid).setValue(user)

                    }else {
                        val newTeam = team(
                            "New Team",
                            region_name,
                            arrayListOf(
                                PokemonDataRegion("ditto"),
                                PokemonDataRegion("ditto"),
                                PokemonDataRegion("ditto")
                            ),
                            java.util.UUID.randomUUID().toString()
                        )
                        user?.teams?.add(newTeam)
                        database.child(auth.currentUser!!.uid).setValue(user)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }



        val ref = database.child(auth.currentUser!!.uid)
        var teams = ArrayList<team>()
        team_recycler?.layoutManager = LinearLayoutManager(this)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    var ids = ArrayList<String>()
                    teams.clear()
                    user?.teams?.forEach {
                        ids.add(it.teamID.toString())
                        if (it.region_name.toString() == region_name)
                            teams.add(it)
                    }
                    adapter = teamAdapter(teams, this@team_manager)
                    userVM = ViewModelProviders.of(this@team_manager).get(teamViewModel::class.java)
                    team_recycler?.adapter = adapter


                }
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        sign_out_button.setOnClickListener {
            startActivity(MainActivity.getLaunchIntent(this))
            FirebaseAuth.getInstance().signOut()
            var mGoogleSignInClient: GoogleSignInClient
            var mGoogleSignInOptions: GoogleSignInOptions
            mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
            mGoogleSignInClient.revokeAccess()
            finish()
        }


        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {

                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {

                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

    }


    override fun onItemClicked(team: team) {
        val intent = Intent(this,team_editor::class.java)
        intent.putExtra("regionName",team.region_name)
        intent.putExtra("teamID",team.teamID)
        intent.putExtra("teamName",team.name)
        startActivity(intent)

    }


}
