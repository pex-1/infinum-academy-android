package infinum.academy2019.shows_danijel_pecek

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

class ProgressBarView : View {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    companion object {
        const val INDETERMINANT_MIN_SWEEP = 15f
        const val RESETTING_ANGLE = 620
    }

    private var mOuterCirclePaint: Paint? = null
    private var mInnerCirclePaint: Paint? = null
    private var mThickness: Float = 0f
    private var mInnerPadding: Float = 20f
    private var mAnimDuration: Int = 0
    private var mOuterCircleRect: RectF = RectF(0f, 0f, 0f, 0f)
    private var mInnerCircleRect: RectF = RectF(0f, 0f, 0f, 0f)

    @ColorInt
    private var mOuterCircleColor: Int = 0
    @ColorInt
    private var mInnerCircleColor: Int = 0

    private var mSteps: Int = 0
    private var mSize: Int = 0
    private var mStartAngle: Float = 0.toFloat()
    private var mIndeterminateSweep: Float = 0.toFloat()
    private var mIndeterminateRotateOffset: Float = 0.toFloat()
    private var mIndeterminateAnimator: AnimatorSet? = null



    private fun init(attrs: AttributeSet?, defStyle: Int) {

        mOuterCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInnerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mOuterCircleRect = RectF()
        mInnerCircleRect = RectF()

        val resources = resources

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ProgressBarView, defStyle, 0
        )
        mThickness = a.getDimensionPixelSize(
            R.styleable.ProgressBarView_dpv_thickness,
            resources.getDimensionPixelSize(R.dimen.default_thickness)
        ).toFloat()
        mInnerPadding = a.getDimensionPixelSize(
            R.styleable.ProgressBarView_dpv_inner_padding,
            resources.getDimensionPixelSize(R.dimen.default_inner_padding)
        ).toFloat()

        mOuterCircleColor = ContextCompat.getColor(context, R.color.main_color)
        mInnerCircleColor = ContextCompat.getColor(context, R.color.main_color)

        mAnimDuration = a.getInteger(
            R.styleable.ProgressBarView_dpv_anim_duration,
            resources.getInteger(R.integer.default_anim_duration)
        )
        mSteps = resources.getInteger(R.integer.default_anim_step)
        mStartAngle = resources.getInteger(R.integer.default_start_angle).toFloat()
        a.recycle()
        setPaint()
    }

    private fun setPaint() {
        mOuterCirclePaint?.color = mOuterCircleColor
        mOuterCirclePaint?.style = Paint.Style.STROKE
        mOuterCirclePaint?.strokeWidth = mThickness
        mOuterCirclePaint?.strokeCap = Paint.Cap.BUTT
        mInnerCirclePaint?.color = mInnerCircleColor
        mInnerCirclePaint?.style = Paint.Style.STROKE
        mInnerCirclePaint?.strokeWidth = mThickness
        mInnerCirclePaint?.strokeCap = Paint.Cap.BUTT
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Draw outer circle progress
        canvas.drawArc(
            mOuterCircleRect, mStartAngle + mIndeterminateRotateOffset,
            mIndeterminateSweep, false, mOuterCirclePaint!!
        )
        //Draw inner circle progress
        canvas.drawArc(
            mInnerCircleRect, mStartAngle + mIndeterminateRotateOffset + 100f,
            mIndeterminateSweep, false, mInnerCirclePaint!!
        )

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val xPad = paddingLeft + paddingRight
        val yPad = paddingTop + paddingBottom
        val width = measuredWidth - xPad
        val height = measuredHeight - yPad
        mSize = if (width < height) width else height
        setMeasuredDimension(mSize + xPad, mSize + yPad)
        updateRectAngleBounds()
    }

    private fun updateRectAngleBounds() {
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        mOuterCircleRect.set(
            paddingLeft + mThickness, paddingTop + mThickness,
            mSize.toFloat() - paddingLeft.toFloat() - mThickness, mSize.toFloat() - paddingTop.toFloat() - mThickness
        )
        mInnerCircleRect.set(
            paddingLeft.toFloat() + mThickness + mInnerPadding,
            paddingTop.toFloat() + mThickness + mInnerPadding,
            mSize.toFloat() - paddingLeft.toFloat() - mThickness - mInnerPadding,
            mSize.toFloat() - paddingTop.toFloat() - mThickness - mInnerPadding
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSize = if (w < h) w else h
        updateRectAngleBounds()
    }

    private fun createIndeterminateAnimator(step: Float): AnimatorSet {

        val maxSweep = 360f * (mSteps - 1) / mSteps + INDETERMINANT_MIN_SWEEP
        val start = -90f + step * (maxSweep - INDETERMINANT_MIN_SWEEP)

        // Extending the front of the arc
        val frontEndExtend = ValueAnimator.ofFloat(
            INDETERMINANT_MIN_SWEEP,
            maxSweep
        )
        frontEndExtend.duration = (mAnimDuration / mSteps / 2).toLong()
        frontEndExtend.interpolator = DecelerateInterpolator(1f)
        frontEndExtend.addUpdateListener { animation ->
            mIndeterminateSweep = animation.animatedValue as Float
            invalidate()
        }
        frontEndExtend.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                frontEndExtend.removeAllListeners()
                frontEndExtend.cancel()
            }
        })

        val rotateAnimator1 = ValueAnimator.ofFloat(
            step * 820f / mSteps,
            (step + .5f) * 720f / mSteps
        )
        rotateAnimator1.duration = (mAnimDuration / mSteps / 2).toLong()
        rotateAnimator1.interpolator = LinearInterpolator()
        rotateAnimator1.addUpdateListener { animation -> mIndeterminateRotateOffset = animation.animatedValue as Float }

        rotateAnimator1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                rotateAnimator1.removeAllListeners()
                rotateAnimator1.cancel()
            }
        })

        val backEndRetract = ValueAnimator.ofFloat(
            start,
            start + maxSweep - INDETERMINANT_MIN_SWEEP
        )
        backEndRetract.duration = (mAnimDuration / mSteps / 2).toLong()
        backEndRetract.interpolator = DecelerateInterpolator(1f)
        backEndRetract.addUpdateListener { animation ->
            mStartAngle = animation.animatedValue as Float
            mIndeterminateSweep = maxSweep - mStartAngle + start
            invalidate()
            if (mStartAngle > RESETTING_ANGLE) {
                resetAnimation()
            }
        }

        backEndRetract.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                backEndRetract.cancel()
                backEndRetract.removeAllListeners()
            }
        })

        val rotateAnimator2 = ValueAnimator.ofFloat(
            (step + .5f) * 720f / mSteps,
            (step + 1) * 720f / mSteps
        )
        rotateAnimator2.duration = (mAnimDuration / mSteps / 2).toLong()
        rotateAnimator2.interpolator = LinearInterpolator()
        rotateAnimator2.addUpdateListener { animation -> mIndeterminateRotateOffset = animation.animatedValue as Float }

        rotateAnimator2.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                rotateAnimator2.removeAllListeners()
                rotateAnimator2.cancel()
            }
        })

        val set = AnimatorSet()
        set.play(frontEndExtend).with(rotateAnimator1)
        set.play(backEndRetract).with(rotateAnimator2).after(rotateAnimator1)
        return set
    }

    private fun resetAnimation() {

        mStartAngle = resources.getInteger(R.integer.default_start_angle).toFloat()

        if (mIndeterminateAnimator != null && mIndeterminateAnimator!!.isRunning) {
            mIndeterminateAnimator?.cancel()
        }
        mIndeterminateSweep = INDETERMINANT_MIN_SWEEP

        mIndeterminateAnimator = AnimatorSet()
        var prevSet: AnimatorSet? = null
        var nextSet: AnimatorSet
        for (k in 0 until mSteps) {
            nextSet = createIndeterminateAnimator(k.toFloat())
            val builder = mIndeterminateAnimator?.play(nextSet)
            if (prevSet != null) {
                builder?.after(prevSet)
            }
            prevSet = nextSet

        }
        mIndeterminateAnimator?.start()
    }

    private fun startAnimation() {
        resetAnimation()
    }

    private fun stopAnimation() {
        if (mIndeterminateAnimator != null) {
            mIndeterminateAnimator?.cancel()
            mIndeterminateAnimator?.removeAllListeners()
            mIndeterminateAnimator = null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

}