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
        }
    }

    fun operatorButtonClick(view: View) {
        val screenTextView: TextView = findViewById(R.id.screenTextView)
        if (view is AppCompatButton) {
            if (!view.text.equals(getString(R.string.equals))) {
                numberA = screenTextView.text.toString().toDouble()
                screenTextView.text = getString(R.string.zero)
                if (view.text.equals(getString(R.string.divide)))
                    operationType = OperationType.DIVIDE
                else if (view.text.equals(getString(R.string.multiply)))
                    operationType = OperationType.MULTIPLY
                else if (view.text.equals(getString(R.string.minus)))
                    operationType = OperationType.MINUS
                else if (view.text.equals(getString(R.string.plus)))
                    operationType = OperationType.PLUS
            } else if (view.text.equals(getString(R.string.equals))) {
                if (operationType == OperationType.NONE)
                    return
                numberB = screenTextView.text.toString().toDouble()
                val operationResult: Double = when (operationType) {
                    OperationType.DIVIDE -> numberA / numberB
                    OperationType.MULTIPLY -> numberA * numberB
                    OperationType.MINUS -> numberA - numberB
                    OperationType.PLUS -> numberA + numberB
                    OperationType.NONE -> 0.0
                }
                Log.d("my_tag", "numberA = $numberA")
                Log.d("my_tag", "operationType = $operationType")
                Log.d("my_tag", "numberB = $numberB")
                screenTextView.text = operationResult.toString()
            }
        }
    }
}