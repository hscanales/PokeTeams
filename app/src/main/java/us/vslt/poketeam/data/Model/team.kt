package us.vslt.poketeam.data.Model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.ArrayList

@IgnoreExtraProperties
data class team(
    val name : String? = null,
    val region_name : String? = null,
    val members : MutableList<PokemonDataRegion>? = null,
    val teamID : String? = null
){
    constructor() : this("","",null,"")
    @Exclude
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "name" to name,
            "region_name" to region_name,
            "members" to members,
            "teamID" to teamID
        )
    }
}

@IgnoreExtraProperties
data class User(
    val id : String? = null,
    val teams : MutableList<team>? = null
){
    constructor() : this("",null)
    @Exclude
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "id" to id,
            "teams" to teams
        )
    }
}