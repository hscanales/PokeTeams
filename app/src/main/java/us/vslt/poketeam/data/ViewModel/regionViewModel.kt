package us.vslt.poketeam.data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.vslt.poketeam.data.Model.region
import us.vslt.poketeam.data.RoomDB
import us.vslt.poketeam.data.repository.regionRepo
import us.vslt.poketeam.service.retrofit

class regionViewModel(private val app: Application): AndroidViewModel(app){

    private val repository : regionRepo
    init {
        val regionDAO = RoomDB.getInstance(app).regionDao()
        repository = regionRepo(regionDAO, retrofit.getRetrofitInstance())
    }

    private suspend fun insert(region: region) = repository.insert(region)

    private  suspend fun nuke() = repository.nuke()

    fun todos() : LiveData<List<region>> = repository.todos()

    fun getRegions() = viewModelScope.launch {
        this@regionViewModel.nuke()
        val response = repository.getRegions().await()


        if(response.isSuccessful)with(response){
            this.body()?.lista?.forEach{
                this@regionViewModel.insert(it)
            }
        }
    }

    private suspend fun nukePokemons() = repository.nukePokemons()

    fun getRegion(name: String) = viewModelScope.launch {
        this@regionViewModel.nukePokemons()
        val response = repository.getPokemonsByRegion(name).await()

    }


}