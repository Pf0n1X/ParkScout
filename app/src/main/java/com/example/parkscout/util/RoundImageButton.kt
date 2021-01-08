package com.example.parkscout.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageButton


class RoundImageButton : AppCompatImageButton {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {}

    override fun onDraw(canvas: Canvas) {
        val clipPath = Path()
        val rect = RectF(0.0F, 0.0F, this.width.toFloat(), this.height.toFloat())
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    companion object {
        var radius = 18.0f
    }
}