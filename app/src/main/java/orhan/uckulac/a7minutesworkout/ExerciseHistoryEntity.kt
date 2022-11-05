package orhan.uckulac.a7minutesworkout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history-table")
data class ExerciseHistoryEntity(
    @PrimaryKey
    val date:String
    )