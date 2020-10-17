package us.vslt.poketeam.data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "region")
data class region(
    @PrimaryKey
    @field:Json(name="name")
    val region_name : String,
    @field:Json(name="url")
    val download_url : String


)

data class regionResponse(
    @field:Json(name="results")
    val lista : List<region>

)
