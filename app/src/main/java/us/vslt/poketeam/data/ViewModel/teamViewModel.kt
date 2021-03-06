package us.vslt.poketeam.data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import us.vslt.poketeam.data.repository.teamRepository

class teamViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repo: teamRepository

    init {
        repo = teamRepository()
    }

}
