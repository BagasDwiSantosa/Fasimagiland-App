package com.arvl.fasimagiland.ui.screen.liststory

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.databinding.ActivityStoryBinding
import com.arvl.fasimagiland.ui.screen.canvas.CanvasActivity

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gestureDetector = GestureDetector(this, SwipeGestureDetector())

        binding.btnGotIt.setOnClickListener {
            val intent = Intent(this, CanvasActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        } else super.onTouchEvent(event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
    }

    private inner class SwipeGestureDetector : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD_VELOCITY = 200
        private val SWIPE_THRESHOLD_DISTANCE = 150

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 != null && e2 != null) {
                val distanceY = e2.y - e1.y
                val velocityYAbs = Math.abs(velocityY)

                if (distanceY > SWIPE_THRESHOLD_DISTANCE && velocityYAbs > SWIPE_THRESHOLD_VELOCITY) {
                    finish()
                    overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
                    return true
                }
            }
            return false
        }
    }
}