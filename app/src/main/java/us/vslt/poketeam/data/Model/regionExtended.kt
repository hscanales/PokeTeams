package us.vslt.poketeam.data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "poke_region")
data class PokemonDataRegion(
    @PrimaryKey
    @field:Json(name = "name")
    val pokemon_name: String = ""
)

data class Pokemon_Entry(
    @field:Json(name = "pokemon_species")
    val pokemon_data: PokemonDataRegion
)


data class Pokedex(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "pokemon_entries")
    val pokemon_entries: List<Pokemon_Entry>
)


data class Pokedex_Entry(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "url")
    val url: String
)

data class regionExtended(
    val pokedexes: List<Pokedex_Entry>,
    val name: String,
    val id: Int,
)