package com.example.weatherapp.view

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.MainViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        var cityName = GET.getString("cityName", "Delhi")?.lowercase()
        binding.edtCityName.setText(cityName)
        mainViewModel.refreshData(cityName!!)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val dateTime = LocalDateTime.of(year,month,day,hour,minute)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        binding.updatedAt.setText(dateTime.format(formatter))

        getLiveData(cityName)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.tvError.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
            binding.llData.visibility = View.GONE


            var cityName = GET.getString("cityName", "Delhi")?.lowercase()
            binding.edtCityName.setText(cityName)
            mainViewModel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.imgSearchCity.setOnClickListener {
            val cityName = binding.edtCityName.text.toString()
            SET.putString("cityName",cityName)
            SET.apply()
            mainViewModel.refreshData(cityName)
            getLiveData(cityName)
            Log.i(TAG,"onCreate:  " + cityName)
        }


    }

    private fun getLiveData(cityName: String){
        mainViewModel.weather_data.observe(this, androidx.lifecycle.Observer { data ->
            data?.let {
                binding.llData.visibility = View.VISIBLE
                val tvCityCode = data.sys.country
                val tvCityName = data.name
                binding.address.setText("$tvCityName, $tvCityCode")

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(binding.countryIcon)

                val tempInt = data.main.temp.toInt()
                binding.tvDegree.text = "$tempInt°C"

                binding.windSpeed.text = data.wind.speed.toString()
                binding.pressureValue.text = data.main.pressure.toString()
                binding.humidityValue.text = "${data.main.humidity.toString()}%"
                binding.countryText.text = cityName.substring(0,1).uppercase() + cityName.substring(1)
                binding.countryCode.text = data.sys.country
                binding.longitudeValue.text = data.coord.lon.toString()

            }

        })

        mainViewModel.statusError.observe(this, androidx.lifecycle.Observer { error ->
            error?.let {
                if (error) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    binding.llData.visibility = View.GONE
                } else {
                    binding.tvError.visibility = View.GONE
                }
            }

        })

        mainViewModel.statusLoading.observe(this, androidx.lifecycle.Observer { loading ->
            loading?.let {
                if (loading) {
                    binding.tvError.visibility = View.GONE
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.llData.visibility = View.GONE
                } else {
                    binding.pbLoading.visibility = View.GONE
                }
            }

        })
    }
}