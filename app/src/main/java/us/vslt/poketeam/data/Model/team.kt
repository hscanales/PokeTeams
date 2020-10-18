package us.vslt.poketeam.data.Model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.ArrayList

@IgnoreExtraProperties
data class team(
    var name : String? = null,
    var region_name : String? = null,
    var members : MutableList<PokemonDataRegion>? = null,
    var teamID : String? = null,
    var active : Boolean? = true
){
    constructor() : this("","",null,"",true)
    @Exclude
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "name" to name,
            "region_name" to region_name,
            "members" to members,
            "teamID" to teamID,
            "active" to active
        )
    }
}

@IgnoreExtraProperties
data class User(
    var id : String? = null,
    var teams : MutableList<team>? = null
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