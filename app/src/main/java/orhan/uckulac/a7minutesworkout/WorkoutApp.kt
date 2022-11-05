package orhan.uckulac.a7minutesworkout

import android.app.Application

class WorkoutApp:Application() {
    val db by lazy { ExerciseHistoryDatabase.getInstance(this)  // only create when its needed
    }
}