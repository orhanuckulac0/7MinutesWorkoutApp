package orhan.uckulac.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import orhan.uckulac.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }

    private var binding: ActivityBmiBinding? = null

    private var currentVisibleView: String = METRIC_UNITS_VIEW  // as selected default view

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

        binding?.btnCalculateUnits?.setOnClickListener {
            calculateBMI()
        }

        makeVisibleMetricUnitsView()
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->  // will give the ID of the checked radio button
            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE

        binding?.tilUsMetricUnitPound?.visibility = View.GONE  // make Us view gone
        binding?.tilUsMetricUnitFeet?.visibility = View.GONE
        binding?.tilUsMetricUnitInch?.visibility = View.GONE

        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etMetricUnitHeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE  // make metric system invisible
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE

        binding?.tilUsMetricUnitPound?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitFeet?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitInch?.visibility = View.VISIBLE

        binding?.etMetricUnitPound?.text!!.clear()
        binding?.etMetricUnitFeet?.text!!.clear()
        binding?.etMetricUnitInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun calculateBMI(){
        if (currentVisibleView == METRIC_UNITS_VIEW){
            if (validateMetricUnits() && currentVisibleView == METRIC_UNITS_VIEW) {
                val weight: Float = (binding?.etMetricUnitWeight?.text.toString()).toFloat()
                val height: Int = (binding?.etMetricUnitHeight?.text.toString()).toInt()
                val metricBmiScore: Float = (weight / height / height) * 10000
                displayBMIResults(metricBmiScore)
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }

        }else{
            if (validateMetricUnits() && currentVisibleView == US_UNITS_VIEW) {
                val pounds: Float = (binding?.etMetricUnitPound?.text.toString()).toFloat()
                val feet = (binding?.etMetricUnitFeet?.text.toString())
                val inch = (binding?.etMetricUnitInch?.text.toString())
                val inchTotal = inch.toFloat() + feet.toFloat()*12
                val usBmiScore: Float = (pounds / (inchTotal*inchTotal)) * 703
                displayBMIResults(usBmiScore)
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if (currentVisibleView == METRIC_UNITS_VIEW){
            if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
                isValid = false
            }else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()){
                isValid = false
            }
        }else {
            if (currentVisibleView == US_UNITS_VIEW){
                if (binding?.etMetricUnitPound?.text.toString().isEmpty()){
                    isValid = false
                }else if (binding?.etMetricUnitFeet?.text.toString().isEmpty()){
                    isValid = false
                }else if (binding?.etMetricUnitInch?.text.toString().isEmpty()) {
                    isValid = false
                }
           }
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

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIType?.text = bmiType
        binding?.tvBMIDescription?.text = bmiDescription
        binding?.tvBMIValue?.text = bmiValue
    }
}
