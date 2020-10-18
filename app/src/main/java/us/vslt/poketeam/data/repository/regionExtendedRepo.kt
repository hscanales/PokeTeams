package us.vslt.poketeam.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Deferred
import retrofit2.Response
import us.vslt.poketeam.data.DAO.regionExtendedDAO
import us.vslt.poketeam.data.Model.Pokedex
import us.vslt.poketeam.data.Model.Pokemon_Regional
import us.vslt.poketeam.data.Model.regionExtended
import us.vslt.poketeam.service.retrofit

class regionExtendedRepo(private val retrofit: retrofit, private val regionDAO: regionExtendedDAO) {
    fun getRegionByName(name: String): Deferred<Response<regionExtended>> {
        return retrofit.getRegionByName(name)
    }

    fun getPokedexByName(name: String): Deferred<Response<Pokedex>> {
        return retrofit.getPokedexByName(name)
    }

    fun getPokemonByName(name: String): Deferred<Response<Pokemon_Regional>> {
        return retrofit.getPokemonRegionalByName(name)
    }

    @WorkerThread
    suspend fun insertRegional(poke: Pokemon_Regional) = regionDAO.insertPokemonRegion(poke)

    @WorkerThread
    suspend fun nuke() = regionDAO.nukePokemons()

    fun getPokemonFromRegion() = regionDAO.getpokemonfromregion()
}