package us.vslt.poketeam.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import us.vslt.poketeam.R
import us.vslt.poketeam.adapters.teamAdapter
import us.vslt.poketeam.data.ViewModel.teamDetalVM
import us.vslt.poketeam.data.ViewModel.teamViewModel

class team_editor : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var userVM: teamDetalVM
    lateinit var adapter: teamAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_editor)
    }
}