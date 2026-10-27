package com.example.thaicheckersfloating

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView
import android.graphics.Color
import android.view.View

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private var panel: LinearLayout? = null
    private lateinit var board: BoardView
    private lateinit var status: TextView
    private var editMode = true

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (panel != null) return START_NOT_STICKY
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(10, 10, 10, 10)
            setBackgroundColor(Color.rgb(250, 243, 229))
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val title = TextView(this).apply {
            text = "หมากฮอสลอย  ⋮"
            textSize = 16f
            setTextColor(Color.rgb(70, 45, 25))
            setPadding(4, 4, 12, 4)
        }
        val close = Button(this).apply {
            text = "×"
            setOnClickListener { stopSelf() }
        }
        header.addView(title, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(close)
        board = BoardView(this)
        status = TextView(this).apply {
            text = "โหมดจัดหมาก: แตะช่องเพื่อวน ว่าง → แดง → ดำ"
            textSize = 12f
            setTextColor(Color.DKGRAY)
            setPadding(4, 6, 4, 6)
        }
        val controls = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val mode = Button(this).apply {
            text = "เล่น/จัดหมาก"
            setOnClickListener {
                editMode = !editMode
                board.editMode = editMode
                status.text = if (editMode) "โหมดจัดหมาก: แตะช่อง ว่าง → แดง → ดำ"
                              else "โหมดเดิน: แตะหมากฝ่ายแดง แล้วแตะช่องปลายทาง"
            }
        }
        val analyze = Button(this).apply {
            text = "AI แนะนำ"
            setOnClickListener {
                val move = CheckersAI.bestMove(board.position, board.redToMove)
                if (move == null) status.text = "ยังไม่พบตาเดิน: ตรวจตำแหน่งหมากและฝ่ายที่เดิน"
                else {
                    board.hint = move
                    board.invalidate()
                    status.text = "แนะนำ: (${move.fromR+1},${move.fromC+1}) → (${move.toR+1},${move.toC+1})"
                }
            }
        }
        val side = Button(this).apply {
            text = "ฝ่ายแดงเดิน"
            setOnClickListener {
                board.redToMove = !board.redToMove
                text = if (board.redToMove) "ฝ่ายแดงเดิน" else "ฝ่ายดำเดิน"
            }
        }
        controls.addView(mode, LinearLayout.LayoutParams(0, -2, 1f))
        controls.addView(analyze, LinearLayout.LayoutParams(0, -2, 1f))
        controls.addView(side, LinearLayout.LayoutParams(0, -2, 1f))
        root.addView(header)
        root.addView(board, LinearLayout.LayoutParams(320, 320))
        root.addView(status)
        root.addView(controls)
        panel = root
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 12; y = 100
        }
        wm.addView(root, params)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        panel?.let { try { wm.removeView(it) } catch (_: Exception) {} }
        panel = null
        super.onDestroy()
    }
}
