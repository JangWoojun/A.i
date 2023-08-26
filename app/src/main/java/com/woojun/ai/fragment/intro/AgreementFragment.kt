package com.woojun.ai.fragment.intro

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentAgreementBinding

class AgreementFragment : Fragment() {

    private var _binding: FragmentAgreementBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgreementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                val dialog = Dialog(requireContext())
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
                val dialog = Dialog(requireContext())
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
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.agreementFragment, true)
                        .build()

                    view.findNavController().navigate(
                        R.id.action_agreementFragment_to_onboardFragment,
                        null,
                        navOptions
                    )
                } else {
                    Toast.makeText(requireContext().applicationContext, "필수 약관들을 모두 동의해주세요", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}