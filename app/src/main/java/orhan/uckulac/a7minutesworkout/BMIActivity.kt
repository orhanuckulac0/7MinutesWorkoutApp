package orhan.uckulac.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import orhan.uckulac.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    private var binding: ActivityBmiBinding? = null
    private var etMetricUnitWeight: AppCompatEditText? = null
    private var etMetricUnitHeight: AppCompatEditText? = null
    private var llDisplayBMIResult: LinearLayout? = null

    private var tvBMIValue: TextView? = null
    private var tvBMIType: TextView? = null
    private var tvBMIDescription: TextView? = null

    private var btnCalculateUnits: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBMIActivity)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)  // activate the back btn
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBMIActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        etMetricUnitWeight = binding?.etMetricUnitWeight
        etMetricUnitHeight = binding?.etMetricUnitHeight
        llDisplayBMIResult = binding?.llDisplayBMIResult

        tvBMIValue = binding?.tvBMIValue
        tvBMIType = binding?.tvBMIType
        tvBMIDescription = binding?.tvBMIDescription

        btnCalculateUnits = binding?.btnCalculateUnits
        btnCalculateUnits?.setOnClickListener {
            if (validateMetricUnits()){
                val weight: Float = (etMetricUnitWeight?.text.toString()).toFloat()
                val height: Int = (etMetricUnitHeight?.text.toString()).toInt()
                val bmiScore: Float = (weight/height/height)*10000
                println(bmiScore)
                displayBMIResults(bmiScore)

            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if (etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if (etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun displayBMIResults(BMI: Float){
        val bmiType: String
        val bmiDescription: String
        val bmiValue = BigDecimal(BMI.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        println(bmiValue)
        if (BMI.compareTo(15f) <= 0) {
            bmiType = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (BMI.compareTo(15f) > 0 && BMI.compareTo(16f) <= 0) {
            bmiType = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (BMI.compareTo(16f) > 0 && BMI.compareTo(18.5f) <= 0) {
            bmiType = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (BMI.compareTo(18.5f) > 0 && BMI.compareTo(25f) <= 0) {
            bmiType = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (BMI.compareTo(25f) > 0 && BMI.compareTo(30f) <= 0) {
            bmiType = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (BMI.compareTo(30f) > 0 && BMI.compareTo(35f) <= 0) {
            bmiType = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (BMI.compareTo(35f) > 0 && BMI.compareTo(40f) <= 0) {
            bmiType = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiType = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult?.visibility = View.VISIBLE
        tvBMIType?.text = bmiType
        tvBMIDescription?.text = bmiDescription
        tvBMIValue?.text = bmiValue
    }
}
