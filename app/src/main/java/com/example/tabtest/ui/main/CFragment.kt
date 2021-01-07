package com.example.tabtest.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tabtest.MainActivity
import com.example.tabtest.R
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.reflect.typeOf

var lat : String = ""
var lng : String = ""
val num_of_rows = 10
val page_no = 1
val data_type = "JSON"
var base_time = "1800"
var base_data = "20210101"
var nx = "56"
var ny = "125"

var tmp: String = ""
var rain: String = ""
var wind: String = ""
var humi: String = ""
var winddriec: String = ""
var rainform: String = ""
var address: String = ""


data class WEATHER (
        val response : RESPONSE
)
data class RESPONSE (
        val header : HEADER,
        val body : BODY
)
data class HEADER(
        val resultCode : Int,
        val resultMsg : String
)
data class BODY(
        val dataType : String,
        val items : ITEMS
)
data class ITEMS(
        val item : List<ITEM>
)
data class ITEM(
        val baseDate : String,
        val baseTime : String,
        val category : String,
        val nx : String,
        val ny : String,
        val obsrValue: String
)

interface WeatherInterface {
    @GET("getUltraSrtNcst?serviceKey=zJI20aUR59nMTl5ApsYTsvuvZAWgThqp745YEiuqUfReu9PD9M7PDvC12UF1ha8uZPNwV75JuwB4mfhUSKOL9A%3D%3D")
    fun GetWeather(
            @Query("dataType") data_type : String,
            @Query("numOfRows") num_of_rows : Int,
            @Query("pageNo") page_no : Int,
            @Query("base_date") base_date : String,
            @Query("base_time") base_time : String,
            @Query("nx") nx : String,
            @Query("ny") ny : String
    ): Call<WEATHER>
}


private val retrofit = Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService/") // 마지막 / 반드시 들어가야 함
        .addConverterFactory(GsonConverterFactory.create()) // converter 지정
        .build() // retrofit 객체 생성

object ApiObject {
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}




class CFragment : Fragment(), FragmentLifecycle {
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd,HHmm")

    var fragC : View? = null

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_c, container, false)
        fragC = root

//        Log.d("tab","resumeC")
//        getCurrentDate()
//        getCurrentPosition()
//        if(lat == "" || lng == "" || address==" "){
//            Toast.makeText(this.requireContext(), "gps is null", Toast.LENGTH_SHORT).show()
//            var count = 0
//            while((lat == "" || lng == "" || address == " ") && count < 1000){
//                sleep(1)
//                count++
////                getCurrentPosition()
//                println(count)
//            }
//        }
//        var addrText = fragC?.findViewById<TextView>(R.id.address)
//        addrText?.text = address
////        Toast.makeText(this.requireContext(), "gps is Ok", Toast.LENGTH_SHORT).show()
//        println("date is $base_data")
//        println("time is $base_time")
//        println("Address is $address")
//        var grid: LatXLngY?
//        if (!(lat == "") && !(lng == "")) {
//            grid = convertGRID_GPS(TO_GRID, lat.toDouble(), lng.toDouble())
//        }
//        else{
//            grid = null
//            Log.d("error","retry load lat and lng")
//        }
//        nx = grid?.x?.toInt().toString()
//        ny = grid?.y?.toInt().toString()
//        println("$nx, $ny")
//        callweather()
//        //초기화
//        //Address
//        //Temperature


        return root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
//        Log.d("tab","resumeC")
//        getCurrentDate()
//        getCurrentPosition()
//        if(lat == "" || lng == ""){
//            Toast.makeText(this.requireContext(), "gps is null", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            Toast.makeText(this.requireContext(), "gps is Ok", Toast.LENGTH_SHORT).show()
//        }
//        println("date is $base_data")
//        println("time is $base_time")
//        println("Address is $address")
//        var grid: LatXLngY?
//
//        if (!(lat == "") && !(lng == "")) {
//            grid = convertGRID_GPS(TO_GRID, lat.toDouble(), lng.toDouble())
//        }
//        else{
//            grid = null
//            Log.d("error","retry load lat and lng")
//        }
//        nx = grid?.x?.toInt().toString()
//        ny = grid?.y?.toInt().toString()
//        println("$nx, $ny")
//        callweather()
//
//        //초기화
//        //Address
//        var addrText = fragC?.findViewById<TextView>(R.id.address)
//        addrText?.text = address

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(){
        var dateAndtime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        var current = dateAndtime.format(formatter)
        current = current.toString()
        val result = current.split(",")
        val currentDate = result[0]
        val currentTime = result[1]
        base_data = currentDate
        base_time = currentTime
    }

    fun getPosition(){
        val act = activity as MainActivity
        lat = act.getLat()
        lng = act.getLng()
        address = act.getAddress()
        println("Latitude : $lat")
        println("Longtitude : $lng")
        println("Address : $address")
    }

    fun getCurrentPosition(){
        val act = activity as MainActivity
        act.getLocation()
        lat = act.getLat()
        lng = act.getLng()
        address = act.getAddress()
        var count = 0
//        while((lat == "" || lng == "" || address == " ") && count < 1000){
//            sleep(1)
//            count++
//            lat = act.getLat()
//            lng = act.getLng()
//            address = act.getAddress()
//            println(count)
//        }
        println("Latitude : $lat")
        println("Longtitude : $lng")
        println("Address : $address")
    }


    override fun onPauseFragment() {
        Log.d("tab","pauseC")
        this.onDestroyView()
    }

    fun getClothes(){
        var temperature : Double? = null
        if(tmp != ""){
            temperature = tmp.toDouble()
        }
        //outer
        val outer_image_1 = fragC?.findViewById<ImageView>(R.id.outer_image_1)
        val outer_text_1 = fragC?.findViewById<TextView>(R.id.outer_text_1)
        val outer_image_2 = fragC?.findViewById<ImageView>(R.id.outer_image_2)
        val outer_text_2 = fragC?.findViewById<TextView>(R.id.outer_text_2)

        //top
        val top_image_1 = fragC?.findViewById<ImageView>(R.id.top_image_1)
        val top_text_1 = fragC?.findViewById<TextView>(R.id.top_text_1)
        val top_image_2 = fragC?.findViewById<ImageView>(R.id.top_image_2)
        val top_text_2 = fragC?.findViewById<TextView>(R.id.top_text_2)

        //bottom
        val bottom_image_1 = fragC?.findViewById<ImageView>(R.id.bottom_image_1)
        val bottom_text_1 = fragC?.findViewById<TextView>(R.id.bottom_text_1)
        val bottom_image_2 = fragC?.findViewById<ImageView>(R.id.bottom_image_2)
        val bottom_text_2 = fragC?.findViewById<TextView>(R.id.bottom_text_2)

        val accessory_image_1 = fragC?.findViewById<ImageView>(R.id.accessory_image_1)
        val accessory_text_1 = fragC?.findViewById<TextView>(R.id.accessory_text_1)
        val accessory_image_2 = fragC?.findViewById<ImageView>(R.id.accessory_image_2)
        val accessory_text_2 = fragC?.findViewById<TextView>(R.id.accessory_text_2)

        if(temperature != null){
            if(temperature >= 28){
                top_text_1?.text = "반팔티"
                top_image_1?.setImageResource(R.drawable.icon_short_sleeve)

                bottom_text_1?.text = "반바지"
                bottom_image_1?.setImageResource(R.drawable.icon_short_pant)
            }
            else if((23 <= temperature) && (temperature < 28)){
                //top
                top_text_1?.text = "반팔티"
                top_image_1?.setImageResource(R.drawable.icon_short_sleeve)
                top_text_2?.text = "셔츠"
                top_image_2?.setImageResource(R.drawable.icon_shirts)

                //bottom
                bottom_text_1?.text = "면바지"
                bottom_image_1?.setImageResource(R.drawable.icon_cotton_trousers)
            }
            else if(20 <= temperature && temperature < 23){
                //outer
                outer_text_1?.text = "가디건"
                outer_image_1?.setImageResource(R.drawable.icon_cardigan)

                //top
                top_text_1?.text = "긴팔티"
                top_image_1?.setImageResource(R.drawable.icon_long_sleeve)
                top_text_2?.text = "셔츠"
                top_image_2?.setImageResource(R.drawable.icon_shirts)

                //bottom
                bottom_text_1?.text = "청바지"
                bottom_image_1?.setImageResource(R.drawable.icon_jean)
                bottom_text_2?.text = "면바지"
                bottom_image_2?.setImageResource(R.drawable.icon_cotton_trousers)

            }
            else if(17 <= temperature && temperature < 20){
                //outer
                outer_text_1?.text = "가디건"
                outer_image_1?.setImageResource(R.drawable.icon_cardigan)

                //top
                top_text_1?.text = "긴팔티"
                top_image_1?.setImageResource(R.drawable.icon_long_sleeve)
                top_text_2?.text = "맨투맨"
                top_image_2?.setImageResource(R.drawable.icon_sweat_shirt)

                //bottom
                bottom_text_1?.text = "청바지"
                bottom_image_1?.setImageResource(R.drawable.icon_jean)
                bottom_text_2?.text = "면바지"
                bottom_image_2?.setImageResource(R.drawable.icon_cotton_trousers)

            }
            else if(12 <= temperature && temperature < 17){
                //outer
                outer_text_1?.text = "자켓"
                outer_image_1?.setImageResource(R.drawable.icon_jacket)
                outer_text_2?.text = "가디건"
                outer_image_2?.setImageResource(R.drawable.icon_cardigan)

                //top
                top_text_1?.text = "긴팔티"
                top_image_1?.setImageResource(R.drawable.icon_long_sleeve)
                top_text_2?.text = "맨투맨"
                top_image_2?.setImageResource(R.drawable.icon_sweat_shirt)

                //bottom
                bottom_text_1?.text = "청바지"
                bottom_image_1?.setImageResource(R.drawable.icon_jean)
                bottom_text_2?.text = "면바지"
                bottom_image_2?.setImageResource(R.drawable.icon_cotton_trousers)

            }
            else if(9 <= temperature && temperature < 12){
                //outer
                outer_text_1?.text = "자켓"
                outer_image_1?.setImageResource(R.drawable.icon_jacket)
                outer_text_2?.text = "트랜치코트"
                outer_image_2?.setImageResource(R.drawable.icon_trench_coat)

                //top
                top_text_1?.text = "니트"
                top_image_1?.setImageResource(R.drawable.icon_knit_wear)
                top_text_2?.text = "맨투맨"
                top_image_2?.setImageResource(R.drawable.icon_sweat_shirt)

                //bottom
                bottom_text_1?.text = "청바지"
                bottom_image_1?.setImageResource(R.drawable.icon_jean)

            }
            else if(5 <= temperature && temperature < 9){
                //outer
                outer_text_1?.text = "코트"
                outer_image_1?.setImageResource(R.drawable.icon_coat)
                outer_text_2?.text = "가죽자켓"
                outer_image_2?.setImageResource(R.drawable.icon_leather_jacket)

                //top
                top_text_1?.text = "니트"
                top_image_1?.setImageResource(R.drawable.icon_knit_wear)
                top_text_2?.text = "후드"
                top_image_2?.setImageResource(R.drawable.icon_hoodie)

                //bottom
                bottom_text_1?.text = "청바지"
                bottom_image_1?.setImageResource(R.drawable.icon_jean)

                //Accessory
                accessory_text_1?.text = "머플러"
                accessory_image_1?.setImageResource(R.drawable.icon_muffler)
            }
            else{
                //outer
                outer_text_1?.text = "코트"
                outer_image_1?.setImageResource(R.drawable.icon_coat)
                outer_text_2?.text = "패딩"
                outer_image_2?.setImageResource(R.drawable.icon_bubble_jacket)

                //top
                top_text_1?.text = "니트"
                top_image_1?.setImageResource(R.drawable.icon_knit_wear)
                top_text_2?.text = "후드"
                top_image_2?.setImageResource(R.drawable.icon_hoodie)

                //bottom
                bottom_text_1?.text = "청바지"
                bottom_image_1?.setImageResource(R.drawable.icon_jean)

                //Accessory
                accessory_text_1?.text = "머플러"
                accessory_image_1?.setImageResource(R.drawable.icon_muffler)
                accessory_text_2?.text = "장갑"
                accessory_image_2?.setImageResource(R.drawable.icon_gloves)

            }
            if(rain != ""){
                if(rain?.toDouble() > 0){
                    fragC?.findViewById<TextView>(R.id.umbrella_text_1)?.text = "O"
                }
                else{
                    fragC?.findViewById<TextView>(R.id.umbrella_text_1)?.text = "X"
                }
            }
            Log.d("Time", "${base_time.toInt()}")
            if(base_time.toInt() <= 1800 && base_time.toInt() > 700){
                //daytime
                fragC?.findViewById<LinearLayout>(R.id.weather)?.setBackgroundResource(R.drawable.daytime_image)
                fragC?.findViewById<TextView>(R.id.temperature)?.setTextColor(Color.BLACK)
                fragC?.findViewById<TextView>(R.id.address)?.setTextColor(Color.BLACK)
                fragC?.findViewById<TextView>(R.id.wind_direction)?.setTextColor(Color.BLACK)
                fragC?.findViewById<TextView>(R.id.wind_speed)?.setTextColor(Color.BLACK)
                fragC?.findViewById<TextView>(R.id.humidity)?.setTextColor(Color.BLACK)
                fragC?.findViewById<TextView>(R.id.precipitation)?.setTextColor(Color.BLACK)
            }
            else{
                fragC?.findViewById<LinearLayout>(R.id.weather)?.setBackgroundResource(R.drawable.night_image)
                fragC?.findViewById<TextView>(R.id.temperature)?.setTextColor(Color.WHITE)
                fragC?.findViewById<TextView>(R.id.address)?.setTextColor(Color.WHITE)
                fragC?.findViewById<TextView>(R.id.wind_direction)?.setTextColor(Color.WHITE)
                fragC?.findViewById<TextView>(R.id.wind_speed)?.setTextColor(Color.WHITE)
                fragC?.findViewById<TextView>(R.id.humidity)?.setTextColor(Color.WHITE)
                fragC?.findViewById<TextView>(R.id.precipitation)?.setTextColor(Color.WHITE)
                fragC?.findViewById<ImageView>(R.id.precipitation_image)?.setColorFilter(Color.WHITE)
                fragC?.findViewById<ImageView>(R.id.wind_direction_image)?.setColorFilter(Color.WHITE)
                fragC?.findViewById<ImageView>(R.id.wind_speed_image)?.setColorFilter(Color.WHITE)
                fragC?.findViewById<ImageView>(R.id.humidity_image)?.setColorFilter(Color.WHITE)
            }
        }
        else{
            Log.d("Error", "Temperature information does not exists")
        }

    }

    @Synchronized fun callweather() {
        val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_data, base_time, nx, ny)
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    println("$base_data, $base_time")
                    Log.d("api", "${response.body()}")
                    if (response.body()?.response?.header?.resultCode == 0) {
                        println("loading weather success")
                        for (item in response!!.body()!!.response.body?.items?.item) {
                            when (item.category) {
                                "T1H" -> {
                                    tmp = item.obsrValue
                                }
                                "RN1" -> {
                                    rain = item.obsrValue
                                }
                                "REH" -> {
                                    humi = item.obsrValue
                                }
                                "PTY" -> {
                                    rainform = item.obsrValue
                                }
                                "VEC" -> {
                                    winddriec = item.obsrValue
                                }
                                "WSD" -> {
                                    wind = item.obsrValue
                                }
                                else -> println("NO NEED")
                            }

                        }

                    } else if (response.body()?.response?.header?.resultCode == 3) {
                        if (base_time.toInt() >= 100) {
                            if ((base_time.toInt() - 100) < 1000) {
                                base_time = "0" + (base_time.toInt() - 100).toString()
                            } else {
                                base_time = (base_time.toInt() - 100).toString()
                            }
                        } else {
                            base_time = "2300"
                        }
                        return callweather()

                    } else {
                        println("FAIL LOAD WEATHER")
                        println("ERROR : ${response.body()?.response?.header?.resultCode}")
                        return callweather()
                    }
                    //                    val tmp: LatXLngY? = convertGRID_GPS(TO_GRID, 36.3741555, 127.3658293)
                    //                    Log.d("grid", tmp!!.x.toString())
                    //                    Log.d("grid", tmp!!.y.toString())
                    //                    Log.d("api", response.body()!!.response.body.items.item.toString())
                    //                    Log.d("api", response.body()!!.response.body.items.item[0].category)
                    println("온도:$tmp, 강수량:$rain, 습도:$humi, 강수형태:$rainform, 풍향:$winddriec, 풍속:$wind")

                    //UI Text
                    if(rainform != "") {
                        var weather_image = fragC?.findViewById<ImageView>(R.id.precipitation_type)
                        when (rainform.toInt()) {
                            0 -> weather_image?.setImageResource(R.drawable.icon_sunny)
                            1 -> weather_image?.setImageResource(R.drawable.icon_rain)
                            2 -> weather_image?.setImageResource(R.drawable.icon_rainsnow)
                            3 -> weather_image?.setImageResource(R.drawable.icon_snow)
                            4 -> weather_image?.setImageResource(R.drawable.icon_rain)
                            5 -> weather_image?.setImageResource(R.drawable.icon_rain)
                            6 -> weather_image?.setImageResource(R.drawable.icon_rainsnow)
                            7 -> weather_image?.setImageResource(R.drawable.icon_snow)
                            else -> Log.d("Error", "Thers's Error in rainform")
                        }
                    }

                    fragC?.findViewById<TextView>(R.id.temperature)?.text = tmp + "°C"
//                    fragC?.findViewById<TextView>(R.id.wind_direction)?.text = winddriec
                    fragC?.findViewById<TextView>(R.id.wind_speed)?.text = wind + "m/s"
                    fragC?.findViewById<TextView>(R.id.precipitation)?.text = rain + "mm"
                    fragC?.findViewById<TextView>(R.id.humidity)?.text = humi + "%"
                    if (winddriec != "") {
                        val wind_int = ((winddriec.toDouble() + 22.5 * 0.5) / 22.5).toInt()
                        var windDir = ""
                        when (wind_int) {
                            0 -> windDir = "N"
                            1 -> windDir = "NNE"
                            2 -> windDir = "NE"
                            3 -> windDir = "ENE"
                            4 -> windDir = "E"
                            5 -> windDir = "ESE"
                            6 -> windDir = "SE"
                            7 -> windDir = "SSE"
                            8 -> windDir = "S"
                            9 -> windDir = "SSW"
                            10 -> windDir = "SW"
                            11 -> windDir = "WSW"
                            12 -> windDir = "W"
                            13 -> windDir = "WNW"
                            14 -> windDir = "NW"
                            15 -> windDir = "NNW"
                            16 -> windDir = "N"
                            else -> "Error"
                        }
                        fragC?.findViewById<TextView>(R.id.wind_direction)?.text =
                            windDir.toString()
                    }
                }
                getClothes()
            }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                println("FAIL LOAD WEATHER")
                return callweather()
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Synchronized override fun onResumeFragment() {
        Log.d("tab","resumeC")
        getCurrentDate()
        getCurrentPosition()
        if(lat == "" || lng == "" || address==" "){
//            Toast.makeText(this.requireContext(), "gps is null", Toast.LENGTH_SHORT).show()
            var count = 0
            while((lat == "" || lng == "" || address == " ") && count < 1000){
                sleep(1)
                count++
                println(count)
            }
        }
        //Address
        var addrText = fragC?.findViewById<TextView>(R.id.address)
        addrText?.text = address
//        Toast.makeText(this.requireContext(), "gps is Ok", Toast.LENGTH_SHORT).show()
        println("date is $base_data")
        println("time is $base_time")
        println("Address is $address")
        var grid: LatXLngY?
        if (!(lat == "") && !(lng == "")) {
            grid = convertGRID_GPS(TO_GRID, lat.toDouble(), lng.toDouble())
        }
        else{
            grid = null
            Log.d("error","retry load lat and lng")
        }
        nx = grid?.x?.toInt().toString()
        ny = grid?.y?.toInt().toString()
        println("$nx, $ny")
        callweather()
        //초기화

        //Temperature
    }


}
