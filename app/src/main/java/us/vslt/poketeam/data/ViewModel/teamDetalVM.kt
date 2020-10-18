package us.vslt.poketeam.data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.vslt.poketeam.data.Model.Pokemon
import us.vslt.poketeam.data.Model.PokemonDataRegion
import us.vslt.poketeam.data.RoomDB
import us.vslt.poketeam.data.repository.pokemonRepo
import us.vslt.poketeam.service.retrofit

class teamDetalVM(private val app: Application) : AndroidViewModel(app) {
    private val repository: pokemonRepo

    init {
        val regionDAO = RoomDB.getInstance(app).pokemonDAO()
        repository = pokemonRepo(regionDAO, retrofit.getRetrofitInstance())
    }

    suspend fun insert(pokemon: Pokemon) = repository.insert(pokemon)

    private suspend fun nuke() = repository.nuke()

    fun todos(): LiveData<List<Pokemon>> = repository.getTodos()

    fun getPokemons(lista: List<PokemonDataRegion>) = viewModelScope.launch {
        this@teamDetalVM.nuke()

        for (pokemonDataRegion in lista) {
            val response = repository.getPokemon(pokemonDataRegion.pokemon_name).await()

            if (response.isSuccessful) with(response) {

                this@teamDetalVM.insert(this.body()!!)
            }

        }
    }

}
