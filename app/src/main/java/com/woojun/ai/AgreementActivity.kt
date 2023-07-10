package com.woojun.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.woojun.ai.databinding.ActivityAgreementBinding

class AgreementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgreementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            startButton.setOnClickListener {
                if (allCheckBox.isChecked) {
                    startActivity(Intent(this@AgreementActivity, SignUpActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this@AgreementActivity, "필수 약관들을 모두 동의해주세요", Toast.LENGTH_SHORT).show()
                }
            }

            allCheckBox.setOnClickListener {
                if (allCheckBox.isChecked) {
                    consentCheckBox1.isChecked = true
                    consentCheckBox2.isChecked = true
                } else {
                    consentCheckBox1.isChecked = false
                    consentCheckBox2.isChecked = false
                }
            }

            consentCheckBox1.setOnClickListener {
                allCheckBox.isChecked = consentCheckBox1.isChecked && consentCheckBox2.isChecked
            }

            consentCheckBox2.setOnClickListener {
                allCheckBox.isChecked = consentCheckBox2.isChecked && consentCheckBox1.isChecked
            }
        }
    }
}