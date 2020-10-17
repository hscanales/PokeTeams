package us.vslt.poketeam.data.repository


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import retrofit2.Response
import us.vslt.poketeam.data.DAO.regionDAO
import us.vslt.poketeam.data.Model.region
import us.vslt.poketeam.data.Model.regionExtended
import us.vslt.poketeam.data.Model.regionResponse
import us.vslt.poketeam.service.retrofit

class regionRepo(private val regionDAO : regionDAO, private  val retrofit: retrofit){
    fun getRegions() : Deferred<Response<regionResponse>> {return retrofit.getRegiones()}

    @WorkerThread
    suspend fun insert(region: region){
        regionDAO.insert(region)
    }

    fun todos():LiveData<List<region>>{
        return regionDAO.getAll()
    }

    @WorkerThread
    suspend fun nuke() = regionDAO.nuke()

    @WorkerThread
    suspend fun  nukePokemons() = regionDAO.nukePokemons()

    fun getPokemonsByRegion(name: String) : Deferred<Response<regionExtended>>{ return retrofit.getRegionByName(name)}
}