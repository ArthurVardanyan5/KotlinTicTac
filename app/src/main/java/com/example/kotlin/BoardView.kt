package com.example.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class BoardView : View {
    private var turn = true
    private val paint = Paint()
    private val paintX = Paint()
    private val paint0 = Paint()
    private lateinit var rectF: Array<Array<RectF?>>
    private lateinit var bytes: Array<ByteArray>

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawLine(canvas)
        for (i in 0..2) {
            for (j in 0..2) {
                onDrawAll(canvas, rectF[i][j], bytes[i][j].toInt())
            }
        }
    }

    fun onDrawLine(canvas: Canvas) {
        val wight = width
        val height = height
        canvas.drawLine(wight / 3f, 0f, wight / 3f, height.toFloat(), paint)
        canvas.drawLine(wight - wight / 3f, 0f, wight - wight / 3f, height.toFloat(), paint)
        canvas.drawLine(0f, height - height / 3f, wight.toFloat(), height - height / 3f, paint)
        canvas.drawLine(0f, height / 3f, wight.toFloat(), height / 3f, paint)
    }

    fun onDrawAll(canvas: Canvas, rectF: RectF?, date: Int) {
        when (date) {
            X -> {
                onDrawX(canvas, rectF)
            }
            O -> {
                onDrawO(canvas, rectF)
            }
        }
    }

    fun onDrawX(canvas: Canvas, rectF: RectF?) {
        canvas.drawLine(rectF!!.left, rectF.top, rectF.right, rectF.bottom, paintX)
        canvas.drawLine(rectF.right, rectF.top, rectF.left, rectF.bottom, paintX)
    }

    fun onDrawO(canvas: Canvas, rectF: RectF?) {
        canvas.drawCircle(rectF!!.centerX(), rectF.centerY(), 100f, paint0)
    }

    private fun init() {
        paint.color = Color.BLACK
        paintX.color = Color.YELLOW
        paintX.strokeWidth = 15f
        paint.strokeWidth = 15f
        rectF = Array(3) { arrayOfNulls<RectF>(3) }
        bytes = Array(3) { ByteArray(3) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            for (i in 0..2) {
                for (j in 0..2) {
                    if (rectF[i][j]!!.contains(event.x, event.y)) {
                        if (turn) {
                            bytes[i][j] = X.toByte()
                        } else {
                            bytes[i][j] = O.toByte()
                        }
                        turn = !turn
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            for (i in 0..2) {
                val left = i * w / 3f
                val right = left + w / 3f
                for (j in 0..2) {
                    val top = j * h / 3f
                    val button = top + h / 3f
                    rectF[i][j] = RectF(left, top, right, button)
                }
            }
        }
    }

    companion object {
        private const val X = -1
        private const val O = 1
    }
}