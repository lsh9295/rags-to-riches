package com.example.ragstoriches

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
import java.lang.StringBuilder

class WinListActivity : AppCompatActivity() {
    lateinit var binding: ActivityWinListBinding
    lateinit var lotto_No: String
    val lotto_number = arrayOf("drwtNo1", "drwtNo2", "drwtNo3", "drwtNo4", "drwtNo5", "drwtNo6", "bnusNo")
    lateinit var jsonObject: JsonObject
    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinListBinding.inflate(layoutInflater)
        requestQueue = Volley.newRequestQueue(applicationContext)

        setContentView(binding.root)

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
                var ymd = jsonObject["drwNoDate"].toString() + " 추첨"
                var money = jsonObject["firstWinamnt"].toString()
                var parsingM = money.toLong() // 당첨금이 21억을 넘을수도 있으므로 long형변환
                var sb = StringBuilder()
                var cnt = 0

                for (i in 0 until lotto_number.size - 1) {
                    str += jsonObject[lotto_number[i]].toString() + "  "
                }
                str += "+  " + jsonObject[lotto_number[lotto_number.size - 1]]

                sb.append("원")
                while (parsingM != 0L) {  // Long 리터럴 접미사 L
                    sb.append(parsingM % 10)
                    parsingM /= 10
                    cnt++
                    when(cnt) {
                        4 -> sb.append(" 만")
                        8 -> sb.append(" 억")
                    }
                }
                binding.tv.setText(str)
                binding.winDay.setText(ymd.replace("\"","").replace("-", "."))
                binding.firstPrize.setText("1등 당첨금 : " + sb.reverse().toString())
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
}
