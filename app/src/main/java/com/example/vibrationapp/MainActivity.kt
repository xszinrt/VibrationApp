package com.example.vibrationapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.appcompat.app.AppCompatActivity
import com.example.vibrationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isVibrating = false

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnVibrate.setOnClickListener {
            if (isVibrating) stopVibration() else startVibration()
        }
    }

    private fun startVibration() {
        isVibrating = true
        binding.btnVibrate.text = "إيقاف الهزاز"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500, 100), 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 500, 100), 0)
        }
    }

    private fun stopVibration() {
        isVibrating = false
        binding.btnVibrate.text = "تشغيل الهزاز"
        vibrator.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
    }
}
