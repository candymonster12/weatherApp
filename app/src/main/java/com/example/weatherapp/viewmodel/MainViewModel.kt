package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.services.WeatherAPIServices
import kotlinx.coroutines.*


private const val TAG = "MainViewModel"

class MainViewModel: ViewModel() {

      private val weatherApiService = WeatherAPIServices()

      private val _weather_data = MutableLiveData<WeatherModel>()

      val weather_data: LiveData<WeatherModel>
      get() = _weather_data

      private val _statusError = MutableLiveData<Boolean>()

      val statusError: LiveData<Boolean>
      get() = _statusError

      private val _statusLoading = MutableLiveData<Boolean>()

      val statusLoading : LiveData<Boolean>
      get() = _statusLoading




fun refreshData(cityName: String){

      getDataFromAPI(cityName)
}


private fun getDataFromAPI(cityName : String) {


      viewModelScope.launch(Dispatchers.Main + SupervisorJob() ) {

            _statusLoading.postValue(true)

//            val deferredValue = async(Dispatchers.IO) {
//
//            }

            try {
                  val tempData = weatherApiService.getDataService(cityName)
                  Log.i(TAG, "$tempData")
                  _statusError.postValue(false)
                  _statusLoading.postValue(false)
                  _weather_data.postValue(tempData.body())
                  Log.i(TAG, "Successful run")


            } catch (error: Throwable){
                  _statusLoading.postValue(false)
                  _statusError.postValue(true)
                  Log.e(TAG, "Error occurred in coroutine while fetching Data from API")
            }

      }

      }
}

