package com.example.ragstoriches

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ragstoriches.databinding.ActivitySaveBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SaveActivity : AppCompatActivity() {
    lateinit var binding: ActivitySaveBinding
    var ballList = ArrayList<Bitmap>() // 1~45 공 이미지 리스트
    var db = Firebase.firestore
    private val itemList = arrayListOf<SaveNum>() // 리스트 아이템 배열
    private val adapter = ListAdapter(itemList) // 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBallList()

        binding.rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rv.adapter = adapter

        db.collection("SaveNumbs")
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                itemList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    val item = SaveNum(document["list1"] as List<Int>,
                        document["list2"] as List<Int>,
                        document["list3"] as List<Int>,
                        document["list4"] as List<Int>,
                        document["list5"] as List<Int>,
                        document["title"] as String)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
            } .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("SaveActivity", "Error getting documents: $exception")
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
}