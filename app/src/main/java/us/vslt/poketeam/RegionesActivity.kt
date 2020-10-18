package us.vslt.poketeam

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_regiones.*
import us.vslt.poketeam.UI.MainActivity
import us.vslt.poketeam.UI.team_manager
import us.vslt.poketeam.adapters.OnItemClickListener
import us.vslt.poketeam.adapters.RegionAdapter
import us.vslt.poketeam.data.Model.region
import us.vslt.poketeam.data.ViewModel.regionViewModel
import java.util.*


class RegionesActivity : AppCompatActivity(), OnItemClickListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var regionAdapter: RegionAdapter
    lateinit var RegionVM: regionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regiones)
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(toolbar)

        binder()
    }

    fun binder() {
        regionAdapter = RegionAdapter(ArrayList(), this)
        RegionVM = ViewModelProviders.of(this).get(regionViewModel::class.java)
        val colum = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            2
        } else {
            3
        }
        region_recycler.apply {

            adapter = this@RegionesActivity.regionAdapter

            layoutManager = GridLayoutManager(this@RegionesActivity, colum)
        }
        RegionVM.todos().observe(this, {
            regionAdapter.updateList(it)
        })

        RegionVM.getRegions()

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
                // Triggered once the drawer closes
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
                // Triggered once the drawer opens
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


    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, RegionesActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onItemClicked(region: region) {
        val intent = Intent(this@RegionesActivity, team_manager::class.java)
        intent.putExtra("region", region.region_name)
        startActivity(intent)

    }

}

