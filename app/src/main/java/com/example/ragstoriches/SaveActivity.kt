package com.example.ragstoriches

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ragstoriches.databinding.ActivitySaveBinding

class SaveActivity : AppCompatActivity() {
    lateinit var binding : ActivitySaveBinding
    var ballList = ArrayList<Bitmap>() // 1~45 공 이미지 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBallList()

        var lottoBoard = intent.getIntArrayExtra("key")

        if(lottoBoard != null) {
            binding.imageView.setImageBitmap(ballList[lottoBoard[0]])
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
}