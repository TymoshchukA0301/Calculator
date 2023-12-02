package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

enum class OperationType {
    DIVIDE, MULTIPLY, MINUS, PLUS, NONE
}

class MainActivity : AppCompatActivity() {
    private var numberA = 0.0
    private var operationType = OperationType.NONE
    private var numberB = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberButtonClick(view: View) {
        val screenTextView: TextView = findViewById(R.id.screenTextView)
        if (view is AppCompatButton) {
            if (view.text.equals(getString(R.string.point))) {
                if (!screenTextView.text.contains(getString(R.string.point)))
                    screenTextView.append(getString(R.string.point))
            } else if (screenTextView.text.equals(getString(R.string.zero)) ||
                (screenTextView.text.startsWith(getString(R.string.zero)) &&
                        screenTextView.text.length == 1)
            ) {
                screenTextView.text = view.text
            } else {
                screenTextView.append(view.text)
            }
            if (operationType != OperationType.NONE)
                unhighlightOperatorButtons()
        }
    }

    fun operatorButtonClick(view: View) {
        val screenTextView: TextView = findViewById(R.id.screenTextView)
        if (view is AppCompatButton) {
            if (!view.text.equals(getString(R.string.equals))) {
                if (operationType == OperationType.NONE) {
                    numberA = screenTextView.text.toString().toDouble()
                    screenTextView.text = getString(R.string.zero)
                }
                if (view.text.equals(getString(R.string.divide)))
                    operationType = OperationType.DIVIDE
                else if (view.text.equals(getString(R.string.multiply)))
                    operationType = OperationType.MULTIPLY
                else if (view.text.equals(getString(R.string.minus)))
                    operationType = OperationType.MINUS
                else if (view.text.equals(getString(R.string.plus)))
                    operationType = OperationType.PLUS
                highlightOperatorButton(operationType)
            } else if (view.text.equals(getString(R.string.equals))) {
                if (operationType == OperationType.NONE)
                    return
                numberB = screenTextView.text.toString().toDouble()
                unhighlightOperatorButtons()
                val operationResult: Double = when (operationType) {
                    OperationType.DIVIDE -> numberA / numberB
                    OperationType.MULTIPLY -> numberA * numberB
                    OperationType.MINUS -> numberA - numberB
                    OperationType.PLUS -> numberA + numberB
                    OperationType.NONE -> 0.0
                }
                Log.d("console", "numberA = $numberA")
                Log.d("console", "operationType = $operationType")
                Log.d("console", "numberB = $numberB")
                Log.d("console", "operationResult = $operationResult")
                screenTextView.text = operationResult.toString()
                operationType = OperationType.NONE
            }
        }
    }

    private fun highlightOperatorButton(operationType: OperationType) {
        unhighlightOperatorButtons()

        val divideButton: AppCompatButton = findViewById(R.id.divideButton)
        val multiplyButton: AppCompatButton = findViewById(R.id.multiplyButton)
        val minusButton: AppCompatButton = findViewById(R.id.minusButton)
        val plusButton: AppCompatButton = findViewById(R.id.plusButton)

        when (operationType) {
            OperationType.DIVIDE -> {
                divideButton.setBackgroundColor(getColor(R.color.operator_button_highlighted))
                divideButton.setTextColor(getColor(R.color.black))
            }

            OperationType.MULTIPLY -> {
                multiplyButton.setBackgroundColor(getColor(R.color.operator_button_highlighted))
                multiplyButton.setTextColor(getColor(R.color.black))
            }

            OperationType.MINUS -> {
                minusButton.setBackgroundColor(getColor(R.color.operator_button_highlighted))
                minusButton.setTextColor(getColor(R.color.black))
            }

            OperationType.PLUS -> {
                plusButton.setBackgroundColor(getColor(R.color.operator_button_highlighted))
                plusButton.setTextColor(getColor(R.color.black))
            }

            OperationType.NONE -> return
        }
    }

    private fun unhighlightOperatorButtons() {
        val divideButton: AppCompatButton = findViewById(R.id.divideButton)
        val multiplyButton: AppCompatButton = findViewById(R.id.multiplyButton)
        val minusButton: AppCompatButton = findViewById(R.id.minusButton)
        val plusButton: AppCompatButton = findViewById(R.id.plusButton)

        divideButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        divideButton.setTextColor(getColor(R.color.white))

        multiplyButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        multiplyButton.setTextColor(getColor(R.color.white))

        minusButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        minusButton.setTextColor(getColor(R.color.white))

        plusButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        plusButton.setTextColor(getColor(R.color.white))
    }
}