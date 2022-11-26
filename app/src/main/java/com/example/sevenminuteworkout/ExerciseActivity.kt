package com.example.sevenminuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sevenminuteworkout.Constants.defaultExerciseList
import com.example.sevenminuteworkout.databinding.ActivityExerciseBinding
import com.example.sevenminuteworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityExerciseBinding? = null
    private var countDownTimer: CountDownTimer? = null
    private var timerProgress = 0
    private var isRestPeriod = true
    private var exerciseTime = 30
    private var restTime = 10
    private var currentExerciseIndex = 0
    private var exercises: ArrayList<Exercise>? = null
    private var tts: TextToSpeech? = null
    private var finishSound: MediaPlayer? = null
    private var countdownSound: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        tts = TextToSpeech(this, this)

        setSupportActionBar(binding?.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbar?.setNavigationOnClickListener{
            customBackbtnDialog()
        }
        exercises = defaultExerciseList()

        setupMediaPlayers()
        setupTimerView()
        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customBackbtnDialog()
    }

    private fun customBackbtnDialog() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        exerciseAdapter = exercises?.let { ExerciseStatusAdapter(it) }
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupMediaPlayers() {
        try {
            var finishSoundUri = Uri.parse(
                "android.resource://com.example.sevenminuteworkout/" +
                        R.raw.completed_tone
            )
            finishSound = MediaPlayer.create(applicationContext, finishSoundUri)
            finishSound?.isLooping = false
        } catch (e: Exception) {
            Log.e("sound init", e.printStackTrace().toString())
        }

        try {
            var countdownSoundUri = Uri.parse(
                "android.resource://com.example.sevenminuteworkout/" +
                        R.raw.countdown
            )
            countdownSound = MediaPlayer.create(applicationContext, countdownSoundUri)
            countdownSound?.isLooping = false
        } catch (e: Exception) {
            Log.e("sound init", e.printStackTrace().toString())
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        if (countDownTimer != null) {
            countDownTimer?.cancel()
            timerProgress = 0
        }
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        if (finishSound != null) {
            finishSound?.stop()
        }
        if (countdownSound != null) {
            countdownSound?.stop()
        }
        binding = null
    }

    private fun setupTimerView() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            timerProgress = 0
        }
        updateView()
        var interval = getExerciseTimeInMs()
        setRestProgressBar(interval)
    }

    private fun setRestProgressBar(interval:Long) {
        countDownTimer = object : CountDownTimer(interval, 1000) {
            override fun onTick(p0: Long) {
                timerProgress++
                var remainingTime = getExerciseTimeInSec() - timerProgress
                binding?.progressBar?.progress = remainingTime
                binding?.tvTimer?.text = (remainingTime).toString()

                if (remainingTime == 0 && finishSound != null) {
                    try {
                        finishSound?.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else if (remainingTime < 4 && countdownSound != null) {
                    try {
                        countdownSound?.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else if (remainingTime % 10 == 0 && !isRestPeriod) {
                    speakText("$remainingTime seconds")
                }
                else if (isRestPeriod && remainingTime == 6) {
                    speakText("Next exercise. " + exercises?.get(currentExerciseIndex)?.getName())
                }
            }

            override fun onFinish() {
                if (!isRestPeriod) {
                    exercises?.get(currentExerciseIndex)?.setIsCompleted(true)
                    exercises?.get(currentExerciseIndex)?.setIsSelected(false)
                    currentExerciseIndex++
                }
                if (currentExerciseIndex < exercises?.size!!) {
                    isRestPeriod = !isRestPeriod
                    setupTimerView()
                    if (!isRestPeriod) {
                        exercises?.get(currentExerciseIndex)?.setIsSelected(true)
                    }
                }
                else {
                    setAllCompletedView()
                }
                exerciseAdapter?.notifyDataSetChanged()
            }
        }.start()
    }

    private fun setAllCompletedView() {
        finish()
        var intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
        startActivity(intent)
    }

    private fun updateView() {
        binding?.progressBar?.max = getExerciseTimeInSec()
        binding?.progressBar?.progress = getExerciseTimeInSec()
        var exercise = exercises?.get(currentExerciseIndex)?.getName()
        if (isRestPeriod) {
            binding?.tvTitle?.text = "GET READY..."
            binding?.ivExerciseEx?.visibility = View.GONE
            binding?.llCountdownTextArea?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding?.tvUpcomingExerciseTitle?.visibility = View.VISIBLE
            binding?.tvUpcomingExerciseName?.text = exercise
            binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
            speakText("Relax")
        }
        else {
            binding?.tvTitle?.text = exercise
            exercises?.get(currentExerciseIndex)?.getImage()?.let {
                binding?.ivExerciseEx?.setImageResource(it)
            }
            binding?.ivExerciseEx?.visibility = View.VISIBLE
            binding?.llCountdownTextArea?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding?.tvUpcomingExerciseTitle?.visibility = View.GONE
            binding?.tvUpcomingExerciseName?.visibility = View.GONE
            speakText("Begin $exercise")

        }
    }

    private fun speakText(text: String) {
        if (tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    private fun getExerciseTimeInMs() : Long {
        if (isRestPeriod) return (getExerciseTimeInSec() * 1000).toLong()
        return (getExerciseTimeInSec() * 1000).toLong()
    }

    private fun getExerciseTimeInSec() : Int {
        if (isRestPeriod) return restTime
        return exerciseTime
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var setLangResult = tts?.setLanguage(Locale.ENGLISH)
            if (setLangResult == TextToSpeech.LANG_MISSING_DATA ||
                    setLangResult == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "Lang not supported...failed to init TTS")
            }
        } else {
            Log.e("TTS", "Failed to init TTS")
        }
    }
}