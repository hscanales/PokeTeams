package us.vslt.poketeam.service


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import us.vslt.poketeam.data.Model.Pokemon
import us.vslt.poketeam.data.Model.regionExtended
import us.vslt.poketeam.data.Model.regionResponse


const val BASE_URL = "https://pokeapi.co/api/v2/"
interface retrofit {

    @GET("region")
    fun getRegiones(): Deferred<Response<regionResponse>>

    @GET("region/{name}")
    fun getRegionByName(@Path("name") name : String): Deferred<Response<regionExtended>>

    @GET("pokemon/{name}")
    fun getPokemonByName(@Path("name") name: String) : Deferred<Response<Pokemon>>


    companion object {
        var INSTANCE: retrofit? = null

        fun getRetrofitInstance() : retrofit{
            if(INSTANCE != null) return INSTANCE!!
            else{
                INSTANCE = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
                    .create(retrofit::class.java)
                return INSTANCE!!
            }
        }

    }

}
