package us.vslt.poketeam.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import retrofit2.Response
import us.vslt.poketeam.data.DAO.pokemonDAO
import us.vslt.poketeam.data.Model.Pokemon
import us.vslt.poketeam.service.retrofit

class pokemonRepo(val pokemonDao: pokemonDAO, val retrofitInstance: retrofit) {
    fun getPokemon(name : String) : Deferred<Response<Pokemon>>{return retrofitInstance.getPokemonByName(name)}

    @WorkerThread
    suspend fun insert(pokemon: Pokemon){
     pokemonDao.insert(pokemon)
    }

    fun getTodos() : LiveData<List<Pokemon>> {
        Log.d("Lista", pokemonDao.getAll().value.toString())
        return pokemonDao.getAll()
    }

    @WorkerThread
    suspend fun  nuke() = pokemonDao.nuke()

}