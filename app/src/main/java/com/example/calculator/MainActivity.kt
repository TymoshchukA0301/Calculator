package com.example.calculator

import android.os.Bundle
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
        val screenTextView: TextView = findViewById<TextView>(R.id.screenTextView)
        if (view is AppCompatButton) {
            if (view.text.equals(getString(R.string.point))) {
                if (!screenTextView.text.contains(getString(R.string.point)))
                    screenTextView.append(getString(R.string.point))
            } else if (screenTextView.text.equals(getString(R.string.zero))) {
                screenTextView.text = view.text
            } else {
                screenTextView.append(view.text)
            }
        }
    }

    fun operatorButtonClick(view: View) {}
}