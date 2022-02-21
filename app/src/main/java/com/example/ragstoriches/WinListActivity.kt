package com.example.ragstoriches


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ragstoriches.databinding.ActivityWinListBinding
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class WinListActivity : AppCompatActivity() {
    lateinit var binding: ActivityWinListBinding
    lateinit var lotto_No: String
    val lotto_number =
        arrayOf("drwtNo1", "drwtNo2", "drwtNo3", "drwtNo4", "drwtNo5", "drwtNo6", "bnusNo")
    var ballList = ArrayList<Bitmap>() // 1~45 공 이미지 리스트
    lateinit var jsonObject: JsonObject
    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinListBinding.inflate(layoutInflater)
        requestQueue = Volley.newRequestQueue(applicationContext)
        setContentView(binding.root)
        getBallList()

        binding.bt.setOnClickListener {
            requestLottoNumber()
        }
    }

    private fun requestLottoNumber() {
        lotto_No = binding.et.text.toString()

        if (lotto_No == "") {
            Toast.makeText(this, "로또 회차를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        var url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=$lotto_No"
        val request = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                jsonObject = JsonParser.parseString(response) as JsonObject
                var str = lotto_No + "회 :  "
                for (i in 0 until lotto_number.size - 1) {
                    str += jsonObject[lotto_number[i]].toString() + "  "
                }
                str += "+  " + jsonObject[lotto_number[lotto_number.size - 1]]
                binding.tv.setText(str)
            }, Response.ErrorListener {
                Toast.makeText(this, "에러", Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                return HashMap()
            }
        }
        request.setShouldCache(false)
        requestQueue?.add(request)
    }

    protected fun getBallList() { // 로또 볼 리스트 비트맵 추가
        for (i in 0..44) {
            val bmp = resources.getIdentifier("lotto_" + (i + 1), "drawable", packageName)
            val bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, bmp),
                70,
                70,
                false
            )
            ballList.add(bitmap)
        }
    }
}
