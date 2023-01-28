package com.macalester.myshoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.macalester.myshoppinglist.databinding.ActivityScrollingBinding
import com.macalester.myshoppinglist.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textAnim = AnimationUtils.loadAnimation(this,
            R.anim.text_anim)

        binding.tvShopping.startAnimation(textAnim)


        textAnim.setAnimationListener(
            object: Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {

                }
                override fun onAnimationEnd(p0: Animation?) {
                    // start a new activity !
                    startActivity(
                        Intent(
                            this@SplashActivity, ScrollingActivity::class.java,
                        ),
                    )
                }
                override fun onAnimationRepeat(p0: Animation?) {
                }
            }
        )


    }
}