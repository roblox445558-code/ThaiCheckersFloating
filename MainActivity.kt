package com.example.thaicheckersfloating

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 32, 24, 32)
            setBackgroundColor(Color.rgb(248, 242, 231))
        }
        val title = TextView(this).apply {
            text = "กระดานหมากฮอสไทยแบบลอย"
            textSize = 24f
            setTextColor(Color.rgb(65, 45, 30))
            gravity = Gravity.CENTER
        }
        val desc = TextView(this).apply {
            text = "จัดหมากด้วยนิ้ว แล้วกด AI แนะนำตาเดิน\nต้นแบบนี้ยังต้องทดสอบกติกาก่อนใช้แข่งขัน"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 18, 0, 24)
            setTextColor(Color.DKGRAY)
        }
        val launch = Button(this).apply {
            text = "เปิดกระดานลอย"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                    startActivity(intent)
                } else {
                    startService(Intent(this@MainActivity, OverlayService::class.java))
                    finish()
                }
            }
        }
        root.addView(title)
        root.addView(desc)
        root.addView(launch)
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        // User returns from Android overlay permission settings and taps the button again.
    }
}
