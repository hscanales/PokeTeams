package us.vslt.poketeam.data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.vslt.poketeam.data.Model.Pokemon_Regional
import us.vslt.poketeam.data.RoomDB
import us.vslt.poketeam.data.repository.regionExtendedRepo
import us.vslt.poketeam.service.retrofit

class RegionExtendedViewModel(private val app: Application) : AndroidViewModel(app) {
    private val repository: regionExtendedRepo

    init {
        val RegionExtendedDAO = RoomDB.getInstance(app).regionExtendedDAO()
        repository = regionExtendedRepo(retrofit.getRetrofitInstance(), RegionExtendedDAO)


    }

    suspend fun insert(pokemon: Pokemon_Regional) = repository.insertRegional(pokemon)
    suspend fun nuke() = repository.nuke()
    fun todos() = repository.getPokemonFromRegion()

    fun getPokemonsByRegion(regionName: String) = viewModelScope.launch {
        this@RegionExtendedViewModel.nuke()
        val region = repository.getRegionByName(regionName).await()
        if (region.isSuccessful) with(region) {
            this.body()!!.pokedexes.forEach {
                var pokedex = repository.getPokedexByName(it.name).await()
                if (pokedex.isSuccessful) {
                    pokedex.body()?.pokemon_entries?.forEach {
                        val pokemon =
                            repository.getPokemonByName(it.pokemon_data.pokemon_name).await()
                        if (pokemon.isSuccessful) {
                            pokemon.body()?.let { it1 -> this@RegionExtendedViewModel.insert(it1) }
                        }
                    }
                }
            }
        }

    }
}

