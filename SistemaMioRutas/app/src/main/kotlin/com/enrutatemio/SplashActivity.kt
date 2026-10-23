package com.enrutatemio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private companion object {
        const val SPLASH_DISPLAY_TIME = 4500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splasmio)

        val texto = findViewById<ImageView>(R.id.texto)
        val layout = findViewById<RelativeLayout>(R.id.lin_lay)

        // Animaciones legacy (mainfadein y splashfadeout)
        val anim = AnimationUtils.loadAnimation(this, R.anim.mainfadein)
        layout.startAnimation(anim)

        val anim2 = AnimationUtils.loadAnimation(this, R.anim.splashfadeout)
        texto.startAnimation(anim2)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DISPLAY_TIME)
    }
}
