package uz.napa.eyecaria.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark


class CustomImageView(context: Context, attributeSet: AttributeSet) :
    AppCompatImageView(context, attributeSet) {
    private val paint = Paint()
    private var faces: List<Face>? = null
    private var imageScaleX: Float = 1f
    private var imageScaleY: Float = 1f
    private var paintColor: Int = Color.GREEN


    fun setLandMark(faces: List<Face>, isNormal: Int, scaleX: Float, scaleY: Float) {
        this.faces = faces
        this.imageScaleX = scaleX
        this.imageScaleY = scaleY
        this.paintColor = isNormal

    }

    fun clearLandMark() {
        faces = null
    }

    private fun translateX(x: Float): Float = x * imageScaleX
    private fun translateY(y: Float): Float = y * imageScaleY

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = paintColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        faces?.forEach { face ->
            val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
            val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)

            canvas.drawRect(
                translateX(face.boundingBox.left.toFloat()),
                translateY(face.boundingBox.top.toFloat()),
                translateX(face.boundingBox.right.toFloat()),
                translateY(face.boundingBox.bottom.toFloat()),
                paint
            )

            canvas.drawRect(
                leftEye!!.position.x * imageScaleX - 50f,
                leftEye.position.y * imageScaleY + 50f,
                leftEye.position.x * imageScaleX + 50f,
                leftEye.position.y * imageScaleY - 50f,
                paint
            )
            canvas.drawRect(
                rightEye!!.position.x * imageScaleX - 50f,
                rightEye.position.y * imageScaleY + 50f,
                rightEye.position.x * imageScaleX + 50f,
                rightEye.position.y * imageScaleY - 50f,
                paint
            )
        }
    }
}