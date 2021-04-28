package com.udacity.button

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import com.udacity.R
import com.udacity.button.ButtonState
import timber.log.Timber
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    // Animator set for animations
    private val animators: AnimatorSet = AnimatorSet()

    // Animator for button
    private lateinit var buttonAnimator : ValueAnimator
    // Linear interpolator for button animator
    private val linearInterpolator = LinearInterpolator()
    // Value for button
    private var percentage = 0f

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (buttonState) {
            ButtonState.Loading -> {
                // Set animator set as started
                Timber.d("Button loading state is active")
                animators.playTogether(buttonAnimator)
                animators.start()
            }
            ButtonState.Completed -> {
                // Cancel animations
                Timber.d("Button completed state is active")
                animators.cancel()
            }
            ButtonState.Clicked -> {
                Timber.d("Button clicked state is active")
            }
        }
    }

    /**
     * Sets the current state of the button
     */
    fun setState(state: ButtonState) {
        buttonState = state
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.DEFAULT
    }


    init {
        // Set clickable
        isClickable = true
        // Setup animator set
        animators.apply {
            duration = 2500
        }
    }


    override fun onDraw(canvas: Canvas?) {
        // Make sure to draw things in order so the Z order of items do not get messed up.
        // Draw background first, then animated backgrounds/objects, then text
        paint.color = Color.argb(128, 12, 199, 25)
        canvas?.drawColor(context.getColor(R.color.colorPrimary))
        // If the button state is current loading, create animation effect
        if (buttonState == ButtonState.Loading) {
            canvas?.drawRect(0f, 0f, percentage, heightSize.toFloat(), paint)
        }
        paint.color = Color.WHITE
        canvas?.drawText(context.getString(R.string.download),
            widthSize / 2f, (heightSize / 2f) + ((paint.descent() - paint.ascent()) / 2) - paint.descent(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
        // Now initialize the ValueAnimator to this new width
        buttonAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat()).apply {
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = linearInterpolator
            addUpdateListener {
                percentage = it.animatedValue as Float
                invalidate()
            }

        }
    }

}