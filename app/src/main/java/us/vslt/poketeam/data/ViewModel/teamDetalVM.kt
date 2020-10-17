package us.vslt.poketeam.data.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.vslt.poketeam.data.DAO.pokemonDAO
import us.vslt.poketeam.data.Model.Pokemon
import us.vslt.poketeam.data.Model.PokemonDataRegion
import us.vslt.poketeam.data.RoomDB
import us.vslt.poketeam.data.repository.pokemonRepo
import us.vslt.poketeam.service.retrofit

class teamDetalVM(private val app: Application): AndroidViewModel(app){
    private val repository : pokemonRepo
    init {
        val regionDAO = RoomDB.getInstance(app).pokemonDAO()
        repository = pokemonRepo(regionDAO, retrofit.getRetrofitInstance())
    }

    suspend fun  insert(pokemon: Pokemon) = repository.insert(pokemon)

    suspend private fun nuke() = repository.nuke()

    fun todos() : LiveData<List<Pokemon>> = repository.getTodos()

    fun getPokemons(lista: List<PokemonDataRegion>) = viewModelScope.launch {
       this@teamDetalVM.nuke()

        for (pokemonDataRegion in lista) {
            val response = repository.getPokemon(pokemonDataRegion.pokemon_name).await()
            Log.d("Lista_name",pokemonDataRegion.pokemon_name)
            if(response.isSuccessful) with(response){
                Log.d("Lista",this.body()!!.name)
                this@teamDetalVM.insert( this.body()!! )
            }

        }
    }

}
