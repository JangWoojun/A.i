package com.woojun.ai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.ai.databinding.ActivityAgreementBinding

class AgreementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgreementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

        }
    }
}