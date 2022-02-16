package com.example.ragstoriches

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navi = findViewById<BottomNavigationView>(R.id.bottomNavi)
        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        navi.run { setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_fragment1 -> {
                    val homeFragment = DrawActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                }
                R.id.item_fragment2 -> {
                    val boardFragment = WinListActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, boardFragment).commit()
                }
                R.id.item_fragment3 -> {
                    val settingFragment = QrActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, settingFragment).commit()
                }
                R.id.item_fragment4 -> {
                    val settingFragment = NoticeActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, settingFragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.item_fragment1
        }
    }
}