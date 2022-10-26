package orhan.uckulac.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import orhan.uckulac.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)  // activate the back btn
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}