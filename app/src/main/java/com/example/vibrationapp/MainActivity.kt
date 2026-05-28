package com.example.vibrationapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibrationAttributes
import android.os.Vibrator
import android.os.VibratorManager
import androidx.appcompat.app.AppCompatActivity
import com.example.vibrationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isVibrating = false

    private fun getVibrator(): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
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
        binding.btnVibrate.setBackgroundColor(0xFFE53935.toInt())

        val vibrator = getVibrator()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - نستخدم RINGTONE لأنه نفس ما تستخدمه المكالمات
            val effect = VibrationEffect.createWaveform(longArrayOf(0, 1000, 200), 0)
            val attrs = VibrationAttributes.Builder()
                .setUsage(VibrationAttributes.USAGE_RINGTONE)
                .build()
            vibrator.vibrate(effect, attrs)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 1000, 200), 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 1000, 200), 0)
        }
    }

    private fun stopVibration() {
        isVibrating = false
        binding.btnVibrate.text = "تشغيل الهزاز"
        binding.btnVibrate.setBackgroundColor(0xFF43A047.toInt())
        getVibrator().cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        try { getVibrator().cancel() } catch (e: Exception) {}
    }
}
