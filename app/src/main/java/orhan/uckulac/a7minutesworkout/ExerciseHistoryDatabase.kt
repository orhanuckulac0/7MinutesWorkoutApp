package orhan.uckulac.a7minutesworkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseHistoryEntity::class], version = 1)
abstract class ExerciseHistoryDatabase: RoomDatabase() {

    abstract fun historyDAO(): ExerciseHistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: ExerciseHistoryDatabase? = null

        fun getInstance(context: Context): ExerciseHistoryDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExerciseHistoryDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

            return instance
            }
        }
    }
}