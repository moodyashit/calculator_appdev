package com.example.calculatorkopotoh

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var firstOperand: Double = 0.0
    private var operation: String = ""
    private var isOperationClicked: Boolean = false
    private var isCalculating: Boolean = false
    private val maxDigits = 9
    private val decimalFormat = DecimalFormat("#.########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)
    }

    fun numberClick(view: View) {
        val number = (view as TextView).text.toString()

        if (isOperationClicked) {
            resultTextView.text = number
            isOperationClicked = false
        } else {
            val currentText = resultTextView.text.toString()

            if (number == "." && currentText.contains(".")) {
                return
            }

            if (currentText == "0" && number != ".") {
                resultTextView.text = number
            } else {
                val nonDecimalText = currentText.replace(".", "")
                if (nonDecimalText.length < maxDigits || currentText.contains(".") && number == ".") {
                    resultTextView.text = currentText + number
                }
            }
        }
    }

    fun operationClick(view: View) {
        val op = (view as TextView).text.toString()

        if (!isCalculating) {
            firstOperand = resultTextView.text.toString().toDouble()
            isCalculating = true
        } else {
            calculate()
            firstOperand = resultTextView.text.toString().toDouble()
        }

        operation = op
        isOperationClicked = true
    }

    fun equalClick(view: View) {
        calculate()
        isCalculating = false
    }

    fun clearClick(view: View) {
        resultTextView.text = "0"
        firstOperand = 0.0
        operation = ""
        isOperationClicked = false
        isCalculating = false
    }

    fun percentClick(view: View) {
        var value = resultTextView.text.toString().toDouble()
        value /= 100.0

        if (isCalculating) {
            value = firstOperand * value
        }

        resultTextView.text = formatResult(value)
    }

    fun changeSignClick(view: View) {
        val value = resultTextView.text.toString().toDouble() * -1
        resultTextView.text = formatResult(value)
    }

    private fun calculate() {
        if (operation.isEmpty() || isOperationClicked) {
            return
        }

        val secondOperand = resultTextView.text.toString().toDouble()
        var result = 0.0

        when (operation) {
            "+" -> result = firstOperand + secondOperand
            "-" -> result = firstOperand - secondOperand
            "×" -> result = firstOperand * secondOperand
            "÷" -> {
                if (secondOperand == 0.0) {
                    resultTextView.text = "Error"
                    return
                }
                result = firstOperand / secondOperand
            }
        }

        resultTextView.text = formatResult(result)
    }

    private fun formatResult(value: Double): String {
        var result = decimalFormat.format(value)

        if (result.replace(".", "").length > maxDigits) {
            return if (value > 0) "Too large" else "Too small"
        }

        if (result.contains(".")) {
            result = result.replace(Regex("\\.0+$"), "")
            result = result.replace(Regex("0+$"), "")
            if (result.endsWith(".")) {
                result = result.substring(0, result.length - 1)
            }
        }

        return result
    }
}