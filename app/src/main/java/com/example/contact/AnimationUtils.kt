package com.example.contact

import android.animation.ObjectAnimator
import android.view.View

object AnimationUtils {
    fun wobbleAnimation(view: View) {
        val wobble = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 25f, -25f, 25f, -25f, 0f)
        wobble.duration = 1000 // 1 second
        wobble.start()
    }
}