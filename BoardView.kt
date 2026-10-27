package com.example.thaicheckersfloating

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.view.MotionEvent
import android.view.View

data class Move(val fromR:Int, val fromC:Int, val toR:Int, val toC:Int, val capture:Boolean=false)

class BoardView(context: Context) : View(context) {
    val position = Array(8) { IntArray(8) }
    var editMode = true
    var redToMove = true
    var hint: Move? = null
    private var selected: Pair<Int,Int>? = null
    private val p = Paint(Paint.ANTI_ALIAS_FLAG)

    init { setLayerType(View.LAYER_TYPE_SOFTWARE, null) }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cell = minOf(width, height) / 8f
        for (r in 0..7) for (c in 0..7) {
            p.color = if ((r+c)%2==0) Color.rgb(242,220,184) else Color.rgb(128,87,58)
            canvas.drawRect(c*cell, r*cell, (c+1)*cell, (r+1)*cell, p)
            if (position[r][c] != 0) {
                p.color = if (position[r][c] > 0) Color.rgb(205,43,57) else Color.rgb(35,35,35)
                canvas.drawCircle((c+.5f)*cell, (r+.5f)*cell, cell*.36f, p)
                p.color = Color.WHITE
                p.style = Paint.Style.STROKE
                p.strokeWidth = 2f
                canvas.drawCircle((c+.5f)*cell, (r+.5f)*cell, cell*.29f, p)
                p.style = Paint.Style.FILL
                if (kings[r][c]) {
                    p.color = Color.YELLOW
                    p.textSize = cell*.45f
                    p.textAlign = Paint.Align.CENTER
                    canvas.drawText("K", (c+.5f)*cell, (r+.66f)*cell, p)
                }
            }
        }
        selected?.let { (r,c) ->
            p.color = Color.YELLOW; p.style = Paint.Style.STROKE; p.strokeWidth = 4f
            canvas.drawRect(c*cell+2, r*cell+2, (c+1)*cell-2, (r+1)*cell-2, p)
            p.style = Paint.Style.FILL
        }
        hint?.let {
            p.color = Color.GREEN; p.style = Paint.Style.STROKE; p.strokeWidth = 4f
            canvas.drawCircle((it.fromC+.5f)*cell, (it.fromR+.5f)*cell, cell*.4f, p)
            canvas.drawCircle((it.toC+.5f)*cell, (it.toR+.5f)*cell, cell*.4f, p)
            p.style = Paint.Style.FILL
        }
    }

    // false = ordinary man, true = king
    val kings = Array(8) { BooleanArray(8) }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_UP) return true
        val cell = minOf(width, height) / 8f
        val c = (event.x / cell).toInt().coerceIn(0,7)
        val r = (event.y / cell).toInt().coerceIn(0,7)
        if ((r+c)%2 == 0) return true
        hint = null
        if (editMode) {
            position[r][c] = when(position[r][c]) { 0 -> 1; 1 -> -1; else -> 0 }
            kings[r][c] = false
        } else {
            val s = selected
            if (s == null) {
                if (position[r][c] != 0 && ((position[r][c] > 0) == redToMove)) selected = r to c
            } else {
                val (sr,sc) = s
                if (sr == r && sc == c) selected = null
                else if (position[r][c] == 0) {
                    position[r][c] = position[sr][sc]
                    kings[r][c] = kings[sr][sc]
                    position[sr][sc] = 0
                    kings[sr][sc] = false
                    if (kings[r][c].not() && ((position[r][c] > 0 && r == 0) || (position[r][c] < 0 && r == 7))) kings[r][c] = true
                    selected = null
                    redToMove = !redToMove
                } else selected = r to c
            }
        }
        invalidate()
        return true
    }
}
