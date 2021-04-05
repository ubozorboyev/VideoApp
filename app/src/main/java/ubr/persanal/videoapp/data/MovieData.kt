package ubr.persanal.videoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "movies")
data class MovieData(
    var name:String,
    val uri: String,
    var isDownload: Boolean,
    var process: Float = 0f,
    var isProcess: Boolean = false,
    var isPause: Boolean = false,
    var workID: UUID ?= UUID.randomUUID(),
    var filePath :String = "",
    var fileSize :Float = 0f,
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0
)