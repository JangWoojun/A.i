package com.woojun.ai

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.Toast
import com.woojun.ai.databinding.ActivityAgreementBinding

class AgreementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgreementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            allCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    allCheckBox.buttonTintList = ColorStateList.valueOf(Color.parseColor("#4894fe"))
                } else {
                    allCheckBox.buttonTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                }
            }

            consentCheckBox1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    consentCheckBox1.buttonTintList = ColorStateList.valueOf(Color.parseColor("#4894fe"))
                } else {
                    consentCheckBox1.buttonTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                }
            }

            consentCheckBox2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    consentCheckBox2.buttonTintList = ColorStateList.valueOf(Color.parseColor("#4894fe"))
                } else {
                    consentCheckBox2.buttonTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                }
            }

            consentButton1.setOnClickListener {
                val dialog = Dialog(this@AgreementActivity)
                dialog.setContentView(R.layout.webview_dialog)
                val windowWidth = resources.displayMetrics.widthPixels * 0.9
                val windowHeight = resources.displayMetrics.heightPixels * 0.7
                dialog.window?.setLayout(windowWidth.toInt(), windowHeight.toInt())

                val ok = dialog.findViewById<CheckBox>(R.id.yes_button)
                val x = dialog.findViewById<CheckBox>(R.id.no_button)
                val webView = dialog.findViewById<WebView>(R.id.webview)

                webView.loadUrl("https://sch10719.neocities.org/codingvoca2")

                ok.setOnClickListener {
                    dialog.dismiss()
                    consentCheckBox1.isChecked = true
                    allCheckBox.isChecked = consentCheckBox1.isChecked && consentCheckBox2.isChecked
                }

                x.setOnClickListener {
                    dialog.dismiss()
                    consentCheckBox1.isChecked = false
                    allCheckBox.isChecked = consentCheckBox1.isChecked && consentCheckBox2.isChecked
                }

                dialog.show()
            }

            consentButton2.setOnClickListener {
                val dialog = Dialog(this@AgreementActivity)
                dialog.setContentView(R.layout.webview_dialog)
                val windowWidth = resources.displayMetrics.widthPixels * 0.9
                val windowHeight = resources.displayMetrics.heightPixels * 0.7
                dialog.window?.setLayout(windowWidth.toInt(), windowHeight.toInt())

                val ok = dialog.findViewById<CheckBox>(R.id.yes_button)
                val x = dialog.findViewById<CheckBox>(R.id.no_button)
                val webView = dialog.findViewById<WebView>(R.id.webview)

                webView.loadUrl("https://sch10719.neocities.org/codingvocaprivacypolicy")

                ok.setOnClickListener {
                    dialog.dismiss()
                    consentCheckBox2.isChecked = true
                    allCheckBox.isChecked = consentCheckBox2.isChecked && consentCheckBox1.isChecked
                }

                x.setOnClickListener {
                    dialog.dismiss()
                    consentCheckBox2.isChecked = false
                    allCheckBox.isChecked = consentCheckBox2.isChecked && consentCheckBox1.isChecked
                }

                dialog.show()
            }

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