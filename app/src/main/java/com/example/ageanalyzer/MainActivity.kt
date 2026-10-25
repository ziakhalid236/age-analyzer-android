package com.example.ageanalyzer

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ageanalyzer.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDatePicker.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
            calculateAge(selectedDate.timeInMillis)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    private fun calculateAge(birthdayMillis: Long) {
        val now = System.currentTimeMillis()
        val diffMillis = now - birthdayMillis

        if (diffMillis < 0) return 

        // Exact time conversions
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

        // Precise Years/Months/Days Calculation
        val birthCal = Calendar.getInstance().apply { timeInMillis = birthdayMillis }
        val nowCal = Calendar.getInstance()

        var years = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
        var months = nowCal.get(Calendar.MONTH) - birthCal.get(Calendar.MONTH)
        var daysLeft = nowCal.get(Calendar.DAY_OF_MONTH) - birthCal.get(Calendar.DAY_OF_MONTH)

        if (daysLeft < 0) {
            months--
            val maxDaysInMonth = nowCal.getActualMaximum(Calendar.DAY_OF_MONTH)
            daysLeft += maxDaysInMonth
        }
        if (months < 0) {
            years--
            months += 12
        }

        // Update UI with formatted strings
        binding.resultCard.visibility = View.VISIBLE
        binding.tvYears.text = "$years @string/years"
        binding.tvMonths.text = "$months @string/months, $daysLeft @string/days"
        binding.tvDetails.text = String.format("%d @string/days | %d @string/hours | %d @string/minutes", days, hours, minutes)
    }
}