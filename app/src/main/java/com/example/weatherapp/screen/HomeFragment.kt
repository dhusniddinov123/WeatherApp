package com.example.weatherapp.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.adapter.Day
import com.example.weatherapp.adapter.DayAdapter
import com.example.weatherapp.adapter.Hour
import com.example.weatherapp.adapter.HourAdapter
import com.example.weatherapp.databinding.FragmentHomeBinding
import org.json.JSONObject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    lateinit var daylist: MutableList<Day>
    lateinit var hourlist: MutableList<Hour>

    val url:String = "http://api.weatherapi.com/v1/forecast.json?key=9bcfb053b7d247fda8c53154230810&q=Tashkent&days=7&aqi=yes&alerts=yes"

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater,container, false)
        val requestque = Volley.newRequestQueue(requireContext())
        daylist = mutableListOf()
        hourlist = mutableListOf()
        // Inflate the layout for this fragment


        val request = JsonObjectRequest(url,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {

                    val location = response?.getJSONObject("location")
                    val name = location?.getString("name")
                    binding.name.text = name

                    val current = response?.getJSONObject("current")
                    val last_updated = current?.getString("last_updated")
                    val temp_c = current?.getString("temp_c")
                    val wind_kph = current?.getString("wind_kph")
                    val humidity = current?.getString("humidity")

                    val condition = current?.getJSONObject("condition")
                    val state = condition?.getString("text")
                    val image = condition?.getString("icon")

                    binding.day.text = "Today , "+last_updated
                    binding.temp.text = temp_c+" °C"
                    binding.wind.text = wind_kph+" km/h"
                    binding.humidity.text = humidity+"%"
                    binding.overcast.text = state
                    binding.state.load("http:"+image)


                    val forecast = response?.getJSONObject("forecast")
                    val forecastday = forecast?.getJSONArray("forecastday")
                    for (i in 0 until forecastday!!.length()){
                        val resObj = forecastday.getJSONObject(i)
                        val day  = resObj.getJSONObject("day")
                        val condition = day.getJSONObject("condition")


                        val date = resObj.getString("date")
                        val n_date = date.substring(date.length-5,date.length)
                        val text = condition.getString("text")
                        val icon = condition.getString("icon")
                        val maxtemp_c = day.getString("maxtemp_c")
                        val mintemp_c = day.getString("mintemp_c")
                        daylist.add(Day(n_date,text,maxtemp_c,mintemp_c,icon))
                        var manager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        binding.fiveday.adapter = DayAdapter(daylist)
                        binding.fiveday.layoutManager = manager



                    }
                    val firstday = forecastday.getJSONObject(0)
                    val hourarr= firstday.getJSONArray("hour")
                    for (i in 0 until hourarr.length()){
                        val resObj = hourarr.getJSONObject(i)
                        val time = resObj.getString("time")
                        var n_time = time.substring(time.length-5, time.length)
                        val temp_c = resObj.getString("temp_c")
                        val wind = resObj.getString("wind_kph")
                        val condition = resObj.getJSONObject("condition")
                        val icon = condition.getString("icon")

                        hourlist.add(Hour(temp_c,icon,wind,n_time))
                        var manager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.hourly.adapter = HourAdapter(hourlist)
                        binding.hourly.layoutManager = manager

                    }



                    Log.d("TAG", "onResponse: $response")
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("TAG", "onErrorResponse: $error")
                }

            })

        requestque.add(request)
        return binding.root


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}