package com.example.ragstoriches

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import com.example.ragstoriches.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding // 뷰 바인딩

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

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
}
