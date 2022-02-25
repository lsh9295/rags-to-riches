package com.example.ragstoriches

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ragstoriches.databinding.ActivityGenerateBinding

class GenerateActivity : AppCompatActivity() {
    lateinit var binding : ActivityGenerateBinding
    var ballList = ArrayList<Bitmap>() // 1~45 공 이미지 리스트
    var lottoBoard = Array(5){ Array(6){ 0 }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateBinding.inflate(layoutInflater);
        setContentView(binding.root)

        getBallList()

        binding.draw.setOnClickListener{
            setBallNum()
        }
        binding.saveNum.setOnClickListener{
            saveBallNum()
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
    private fun setBallNum() {
        val range = (1 .. 45)
        var j = 0
        lateinit var image : ImageView
        for (i in 0 .. 4) {
            while(j != 6) {  //  중복 방지
                lottoBoard[i][j] = range.random()
                for (k in 0 until j) {
                    if (lottoBoard[i][k] == lottoBoard[i][j]) { // 전에 뽑았던 수가 있으면 다시 뽑기
                        j--
                        break
                    }
                }
                j++
            }
            j = 0
            lottoBoard[i].sort()
        }
        for (i in 1 .. 5) {
            for (j in 1 .. 6) {
                var tmpID = resources.getIdentifier("list${i}ball${j}", "id",packageName)
                image = findViewById<View>(tmpID) as ImageView
                image.setImageBitmap(ballList[lottoBoard[i - 1][j - 1] - 1])
            }
        }
    }
    private fun saveBallNum() {

    }
}