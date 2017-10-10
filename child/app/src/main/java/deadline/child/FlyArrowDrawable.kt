package deadline.child

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.IntRange
import android.view.animation.AccelerateInterpolator

/**
 * @author deadline
 * *
 * @time 2017/9/1
 */

open class FlyArrowDrawable(context: Context) : Drawable() {

    private val mPaint: Paint
    private val mPath: Path
    private var color: Int = 0
    private var factor: Float = 0.toFloat()
    private var halfLength: Float = 0.toFloat()
    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat()
    private var valueAnimator: ValueAnimator? = null

    companion object {

        val DEF_COLOR = Color.WHITE
    }

    init {
        halfLength = dip2px(context, 15f).toFloat()
        color = DEF_COLOR

        mPath = Path()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = color
    }

    fun startUp() {
        stop()
        executeAnimation(factor, 1f, 200)
    }

    fun startDown() {
        stop()
        executeAnimation(factor, -1f, 200)
    }

    fun stop() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
            valueAnimator = null
        }
    }

    private fun executeAnimation(from: Float, to: Float, duration: Int) {
        valueAnimator = ValueAnimator.ofFloat(from, to)
        valueAnimator?.duration = duration.toLong()
        valueAnimator?.interpolator = AccelerateInterpolator()
        valueAnimator?.addUpdateListener {
            factor = valueAnimator?.animatedValue as Float
            invalidateSelf()
        }
    }

    override fun draw(canvas: Canvas) {
        val height = ((factor * halfLength).toDouble() / 2.0 / Math.sqrt(2.0)).toInt()
        mPath.reset()
        mPath.moveTo(mCenterX - halfLength, mCenterY + height)
        mPath.lineTo(mCenterX, mCenterY - height)
        mPath.lineTo(mCenterX + halfLength, mCenterY + height)
        mPath.close()
        canvas.drawPath(mPath, mPaint)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mCenterX = bounds.centerX().toFloat()
        mCenterY = bounds.centerY().toFloat()
    }

    fun setHalfLength(halfLength: Int) {
        this.halfLength = halfLength.toFloat()
        invalidateSelf()
    }

    fun setColor(color: Int) {
        this.color = color
        invalidateSelf()
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int) {
        mPaint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    private fun dip2px(c: Context, dpValue: Float): Int {
        val scale = c.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
