package orhan.uckulac.a7minutesworkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import orhan.uckulac.a7minutesworkout.databinding.ActivityExerciseBinding
import orhan.uckulac.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var tts: TextToSpeech? = null
    private var mediaPlayer: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    private var timer: CountDownTimer? = null
    private var restTimerDuration: Long = 10000
    private var exerciseTimerDuration: Long = 30000
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

        tts = TextToSpeech(this, this)
        setupExerciseStatusRecyclerView()
        setupRestView()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)  // create the adaptor
        binding?.rvExerciseStatus?.adapter = exerciseAdapter  // assign the adapter to the recycler view
    }


    private fun setupRestView(){
        if (timer != null){
            timer?.cancel()
            restProgress = 0
        }
        endOfExerciseSound()
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

        timer = object : CountDownTimer(restTimerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar?.progress = 11 - restProgress
                tvTimer?.text = (11 - restProgress).toString()
                tvTitle?.text = getString(R.string.get_ready_text)

                // if current progress, which is the timer text hits 0, cancel timer and initalize onFinish fun
                if (11 - restProgress == 0 || 11 - restProgress < 0){
                    timer?.cancel()
                    onFinish()
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                restProgress = 0
                exerciseList!![currentExercisePosition].setIsSelected(true)  // set the current exercise as selected to show on recycler view
                exerciseAdapter!!.notifyDataSetChanged()

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

        speakOut(currentExercise.getName())  // speak out the exercise name when It's started

        timer = object : CountDownTimer(exerciseTimerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar?.progress = 31 - restProgress
                tvTimer?.text = (31 - restProgress).toString()

                if (31 - restProgress == 0 || 31 - restProgress < 0){
                    timer?.cancel()
                    onFinish()
                    }
                }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(false)  // set the current exercise as as not selected
                exerciseList!![currentExercisePosition].setIsCompleted(true)  // then make it completed
                exerciseAdapter!!.notifyDataSetChanged()

                if (currentExercisePosition < exerciseList!!.size-1 ){
                    speakOut("Rest for 10 seconds.")
                    Toast.makeText(this@ExerciseActivity, "Rest 10 Seconds", Toast.LENGTH_LONG).show()
                    setupRestView()
                }else if (currentExercisePosition == exerciseList!!.size - 1){
                    speakOut("Congratulations!")
                    // wait 3 secs for speak out to finish, then redirect to FinishActivity
                    Timer().schedule(2000) {
                        val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }.start()
    }

    private fun speakOut(text: String){
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported!")
            }else{
                Log.e("TTS", "Initialization Failed!")
            }
        }
    }

    private fun endOfExerciseSound() {
        try {
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.press_start)
            mediaPlayer?.isLooping = false
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss() // Dialog will be dismissed
        }

        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
            timer?.start()  // start the timer again
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }

    override fun onBackPressed() {
        timer?.cancel()  // stop the timer when back is pressed
        customDialogForBackButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null){
            timer?.cancel()
            restProgress = 0
            currentExercisePosition = -1
        }

        // make sure to stop text to speech after app is closed
        if (tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        // stop media player when app is closed
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer = null
        }
            binding = null
    }
}