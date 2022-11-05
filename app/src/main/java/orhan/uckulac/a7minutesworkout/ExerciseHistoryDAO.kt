package orhan.uckulac.a7minutesworkout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseHistoryDAO {
    @Insert
    suspend fun insert(exerciseHistoryEntity: ExerciseHistoryEntity)

    @Query("SELECT * FROM 'history-table'")
    fun fetchAllHistory(): Flow<List<ExerciseHistoryEntity>>
}