package us.vslt.poketeam.data.Model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json

@Entity(tableName = "pokemon")
data class Pokemon(

    @field:Json(name = "name")
    var name : String,
    @field:Json(name = "id")
    var id : Int,
    @field:Json(name= "sprites")
    @TypeConverters(SpriteConverter::class)
    var sprites : Sprites,
    @PrimaryKey
    var uuid : String


){

    constructor() : this("",-1,Sprites(),""){
        this.uuid = java.util.UUID.randomUUID().toString()
    }

    fun toMap() : Map<String,Any?>{
        return mapOf(
            "name" to name,
            "id" to id,
            "sprites" to sprites,

        )

    }
}

data class pokeTypes(
    @field:Json(name="type")
    @TypeConverters(pokeTypeConverter::class)
    val type: pokeType
){
    constructor() : this(pokeType())

    fun toMap() : Map<String,Any?>{
        return mapOf(
            "type" to type
        )
    }
}
data class pokeType (
    @field:Json(name="name")
    val name: String
){
    constructor() : this("")
    fun toMap(): Map<String,Any?>{
        return  mapOf(
            "name" to name
        )
    }
}

data class Sprites(
    @field:Json(name= "front_default")
    val front_default : String
){
    constructor() : this("")
    fun toMap() : Map<String,Any>{
        return mapOf(
            "front_default" to front_default
        )
    }
}



class SpriteConverter{
    private val gson = Gson()
    @TypeConverter
    fun stringToSprite(data : String?): Sprites{
        if(data==null){
            return Sprites()
        }
        val spriteType = object : TypeToken<Sprites>(){}.type

        return gson.fromJson<Sprites>(data,spriteType)


    }
    @TypeConverter
    fun spriteToString(aux : Sprites) : String{
        return gson.toJson(aux)
    }
}

class pokeTypesConverter{
    private val gson = Gson()
    @TypeConverter
    fun stringTopokeType(data : String?): ArrayList<pokeTypes> {
        if(data==null){
            return arrayListOf()
        }
        val pokeTypesType = object : TypeToken<ArrayList<pokeTypes>>(){
        }.type

        return gson.fromJson<ArrayList<pokeTypes>>(data,pokeTypesType)
    }


    @TypeConverter
    fun pokeTypesToString(aux : ArrayList<pokeTypes>) : String{
        return gson.toJson(aux)
    }
}

class pokeTypeConverter{
    private val gson = Gson()
    @TypeConverter
    fun stringTopokeType(data : String?): pokeType{
        if(data==null){
            return pokeType()
        }
        val pokeTypesType = object : TypeToken<pokeType>() {

        }.type

        return gson.fromJson<pokeType>(data,pokeTypesType)
    }


    @TypeConverter
    fun pokeTypesToString(aux : pokeType) : String{
        return gson.toJson(aux)
    }
}
