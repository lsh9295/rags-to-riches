package com.example.ragstoriches

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ragstoriches.databinding.ActivityGenerateBinding

class GenerateActivity : AppCompatActivity() {
    lateinit var binding : ActivityGenerateBinding
    var ballList = ArrayList<Bitmap>() // 1~45 공 이미지 리스트
    var lottoBoard = Array(5){ IntArray(6){ 0 }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateBinding.inflate(layoutInflater);
        setContentView(binding.root)

        getBallList()

        binding.draw.setOnClickListener{
            setBallNum()
        }
        binding.saveNum.setOnClickListener {
            // 다이얼로그를 생성하기 위한 Builder 클래스 생성자
            val builder = AlertDialog.Builder(this)
            builder.setTitle("번호 저장")
                .setMessage("저장하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        saveBallNum()
                        Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }
    private fun getBallList() {
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
            while(j != 6) {
                lottoBoard[i][j] = range.random()
                for (k in 0 until j) {  //  중복 방지
                    if (lottoBoard[i][k] == lottoBoard[i][j]) { // 전에 뽑았던 수가 있으면 다시 뽑기
                        j--
                        break
                    }
                }
                j++
            }
            j = 0
            lottoBoard[i].sort() // 오름차순 정렬
        }
        for (i in 1 .. 5) {
            for (j in 1 .. 6) {
                var tmpID = resources.getIdentifier("list${i}ball${j}", "id", packageName)
                image = findViewById<View>(tmpID) as ImageView
                image.setImageBitmap(ballList[lottoBoard[i - 1][j - 1] - 1])
            }
        }
    }
    private fun saveBallNum() {
        val intent = Intent(this, SaveActivity::class.java)
        intent.putExtra("key", lottoBoard[0])
        startActivity(intent)
    }
}