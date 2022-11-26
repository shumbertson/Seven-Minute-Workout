package com.example.sevenminuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sevenminuteworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {
    private var binding: ActivityBmiBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbBmi)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Back"
        }
        binding?.tbBmi?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.btnCalculate?.setOnClickListener {
            if (inputsAreValid()) {
                var height: Float = binding?.etHeight?.text.toString().toFloat()
                var weight: Float = binding?.etWeight?.text.toString().toFloat()

                val bmi = weight / (height * height) * 703
                displayResult(bmi)
            } else {
                Toast
                    .makeText(this@BmiActivity,
                        "Please check the values you have entered",
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayResult(bmiResult: Float) {
        binding?.tvResultTitle?.visibility = View.VISIBLE
        binding?.tvCalculatedBmi?.visibility = View.VISIBLE
        binding?.tvCalculatedBmi?.text =  BigDecimal(bmiResult.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.tvBmiCategory?.visibility = View.VISIBLE
        binding?.tvBmiCategory?.text = getBmiCategory(bmiResult)
    }

    private fun getBmiCategory(bmi: Float) : String {
        var result = ""
        if (bmi < 18.5) {
            result = "Underweight"
        } else if (bmi >= 18.5 && bmi < 25) {
            result = "Normal"
        } else if (bmi >= 25 && bmi < 30) {
            result = "Overweight"
        } else if (bmi >= 30 && bmi < 40) {
            result = "Obese"
        } else {
            result = "Morbidly Obese"
        }
        return result
    }

    private fun inputsAreValid() : Boolean {
        return binding?.etWeight?.text?.toString()?.isNullOrBlank() == false
                && binding?.etHeight?.text.toString()?.isNullOrBlank() == false
    }
}