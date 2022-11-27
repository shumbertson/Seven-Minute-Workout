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

        binding?.rbImperialUnits?.setOnClickListener {
            binding?.etWeight?.hint = getString(R.string.weight_lbs)
            binding?.tilWeight?.hint = getString(R.string.weight_lbs)
            binding?.etHeight?.hint = getString(R.string.height_feet)
            binding?.tilHeight?.hint = getString(R.string.height_feet)
            binding?.tilHeightInches?.visibility = View.VISIBLE
        }

        binding?.rbMetricUnits?.setOnClickListener {
            binding?.etWeight?.hint = getString(R.string.weight_kgs)
            binding?.tilWeight?.hint = getString(R.string.weight_kgs)
            binding?.etHeight?.hint = getString(R.string.height_cm)
            binding?.tilHeight?.hint = getString(R.string.height_cm)
            binding?.tilHeightInches?.visibility = View.GONE
        }

        binding?.btnCalculate?.setOnClickListener {
            var isImperial = binding?.rbImperialUnits?.isChecked
            if (inputsAreValid(isImperial)) {
                val bmi = calculateBmi(isImperial)
                displayResult(bmi)
            } else {
                Toast
                    .makeText(this@BmiActivity,
                        "Please check the values you have entered",
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calculateBmi(isImperial: Boolean?): Float {
        var height: Float = binding?.etHeight?.text.toString().toFloat()
        var weight: Float = binding?.etWeight?.text.toString().toFloat()
        if (isImperial == true) {
            var inches = binding?.etHeightInches?.text.toString().toFloat()
            var totalHeightInInches = (height * 12) + inches
            return weight / (totalHeightInInches * totalHeightInInches) * 703
        }
        return (weight / (height * height * 0.0001)).toFloat()
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

    private fun inputsAreValid(isImperial: Boolean?) : Boolean {
        if (isImperial == true) {
            return binding?.etWeight?.text?.toString()?.isNullOrBlank() == false
                    && binding?.etHeight?.text.toString()?.isNullOrBlank() == false
                    && binding?.etHeightInches?.text.toString()?.isNullOrBlank() == false
        }
        return binding?.etWeight?.text?.toString()?.isNullOrBlank() == false
                && binding?.etHeight?.text.toString()?.isNullOrBlank() == false
    }
}