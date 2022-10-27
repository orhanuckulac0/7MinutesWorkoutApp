package orhan.uckulac.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import orhan.uckulac.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    var progressBar: ProgressBar? = null
    var tvTimer: TextView? = null
    var tvTitle: TextView? = null
    var ivExerciseImage: ImageView? = null
    var tvNextExerciseLabel: TextView? = null
    var tvNextExerciseName: TextView? = null


    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1  // when increment, it will be 0 which is the starting index of the ArrayList

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

        // set bindings
        progressBar = binding?.progressBar
        tvTimer = binding?.tvTimer
        tvTitle = binding?.tvTitle
        ivExerciseImage = binding?.ivExerciseImage
        tvNextExerciseLabel = binding?.tvUpcomingLabel
        tvNextExerciseName = binding?.tvUpcomingExerciseName

        exerciseList = Constants.defaultExerciseList()  // get all the exercises from Constants

        setupRestView()
    }
    private fun setupRestView(){
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun setRestProgressBar(){
        currentExercisePosition++  // increase current pos by 1 each turn

        progressBar?.progress = restProgress
        progressBar?.max = 10

        ivExerciseImage?.visibility = View.GONE  // make the exercise image visibility GONE while resting
        tvNextExerciseName?.visibility = View.VISIBLE  // return back to visible on rest
        tvNextExerciseLabel?.visibility = View.VISIBLE // return back to visible on rest

        tvNextExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        restTimer = object : CountDownTimer(1100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar?.progress = 11 - restProgress
                tvTimer?.text = (11 - restProgress).toString()
                tvTitle?.text = "Get Ready!"
            }

            override fun onFinish() {
                restProgress = 0
                startExerciseTimer()
            }
        }.start()
    }

    private fun startExerciseTimer(){
        ivExerciseImage?.visibility = View.VISIBLE // make exercise image visible on exercise
        tvNextExerciseName?.visibility = View.GONE  // make exercise title gone on exercise
        tvNextExerciseLabel?.visibility = View.GONE  // make exercise name gone on exercise

        progressBar?.progress = restProgress
        progressBar?.max = 30

        val currentExercise = exerciseList!![currentExercisePosition]  // get current exercise
        tvTitle?.text = currentExercise.getName()  // use getter to get exercise name to display it
        ivExerciseImage?.setImageResource(currentExercise.getImage())  // set exercise image


        restTimer = object : CountDownTimer(3100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar?.progress = 31 - restProgress
                tvTimer?.text = (31 - restProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList!!.size-1 ){
                    Toast.makeText(this@ExerciseActivity, "Rest 10 Seconds", Toast.LENGTH_LONG).show()
                    setupRestView()
                }else
                    Toast.makeText(this@ExerciseActivity, "Congratulations! You have completed all the exercises!", Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
            currentExercisePosition = -1
        }
        binding = null
    }
}