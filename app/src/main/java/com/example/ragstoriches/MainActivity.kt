package com.example.ragstoriches

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ragstoriches.databinding.ActivityMainBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding // 뷰 바인딩
    var ballList = ArrayList<Bitmap>() // 1~45 공 이미지 리스트
    lateinit var round : String // 직전회차
    lateinit var date : String // 추첨날짜
    lateinit var prize : String // 당첨금
    lateinit var imageView : ImageView
    lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        getBallList()
        getLottoNum()

        // 당첨번호 생성
        binding.generate.setOnClickListener {
            val intent = Intent(this, GenerateActivity::class.java)
            startActivity(intent)
        }
        // 저장번호 목록
        binding.save.setOnClickListener {
            val intent = Intent(this, SaveActivity::class.java)
            startActivity(intent)
        }
        // 회차별 당첨번호
        binding.winList.setOnClickListener {
            val intent = Intent(this, WinListActivity::class.java)
            startActivity(intent)
        }
        // QR코드 당첨확인
        binding.qrcode.setOnClickListener {
            val intent = Intent(this, QrActivity::class.java)
            startActivity(intent)
        }
        // 개발자 후기게시판
        binding.review.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://seungheezz.tistory.com/category/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C%20%EA%B0%9C%EB%B0%9C/%EC%9D%B8%EC%83%9D%EC%97%AD%EC%A0%84%20%EC%B6%94%EC%B2%A8%20%ED%9B%84%EA%B8%B0"))
            startActivity(intent)
        }
    }
    private fun getBallList() { // 로또 볼 리스트 비트맵 추가
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
    private fun getLottoNum() { // 직전회차 당첨번호 크롤링
        val bundle = Bundle()
        val nums = ArrayList<Int>()

        object : Thread() {
            override fun run() {
                val doc: Document
                try {
                    doc = Jsoup.connect("https://dhlottery.co.kr/common.do?method=main&mainMode=default").get()
                    var contents = doc.select("#lottoDrwNo") // 회차
                    round = contents.text() + "회차 당첨번호"

                    for (i in 1..6) {
                        contents = doc.select("#drwtNo$i") // 당첨번호
                        nums.add(contents.text().toInt() - 1)
                    }
                    contents = doc.select("#bnusNo") // 보너스번호
                    nums.add(contents.text().toInt() - 1)

                    contents = doc.select("#drwNoDate") // 추첨날짜
                    date = contents.text() + " 추첨"

                    contents = doc.select("#winnerId") // 당첨금
                    prize = contents.text()

                    bundle.putIntegerArrayList("nums", nums)
                    val msg = handler.obtainMessage()
                    msg.data = bundle
                    handler.sendMessage(msg)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }
    // 로또 번호 View에 전달해주는 Handler
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val nums = bundle.getIntegerArrayList("nums")
            // 회차
            textView = binding.roundnum // 회차
            textView.text = round
            
            textView = binding.date // 날짜
            textView.text = date

            textView = binding.winPrize // 날짜
            textView.text = prize

            for (i in 0..6) {   // 당첨 볼
                val num = nums!![i]
                val tmpID = resources.getIdentifier(
                    "ballView${i + 1}", "id",
                    packageName
                )
                imageView = findViewById<View>(tmpID) as ImageView
                imageView.setImageBitmap(ballList[num])
            }
        }
    }
}
