package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.internal.TextScale
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

enum class OperationType {
    POWER, DIVIDE, MULTIPLY, MINUS, PLUS, NONE
}

// Тексти повідомлень про помилки
const val lessThanZero = "Number is less than 0"
const val notInteger = "Number is not integer"
const val divisionByZero = "Can't divide on 0"

class MainActivity : AppCompatActivity() {
    // Число А
    private var numberA = 0.0
    // Математичний оператор
    private var operationType = OperationType.NONE
    // Число В
    private var numberB = 0.0
    // Об'єкт ScreenTextView
    private lateinit var screenTextView: TextView
    // Чи відображається зараз помилка
    private var displayingError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screenTextView = findViewById(R.id.screenTextView)
    }

    fun numberButtonClick(view: View) {
        // Якщо натиснутий елемент має тип AppCompatButton
        if (view is AppCompatButton) {

            // Якщо натиснута кнопка '.'
            if (view.id == R.id.pointButton) {
                // Якщо на screenTextView відображений текст помилки,
                // ігноруємо це натискання
                if (displayingError)
                    return
                // Перевіряємо, чи в тексті на screenTextView є '.'
                if (!screenTextView.text.contains(getString(R.string.point))) {
                    // Дописуємо '.' в текст на screenTextView
                    screenTextView.append(getString(R.string.point))
                }
            }

            // Якщо в тексті на screenTextView є лише '0'
            else if (screenTextView.text.startsWith(getString(R.string.zero)) &&
                screenTextView.text.length == 1
            ) {
                // Замінюємо '0' на текст натиснутої кнопки
                screenTextView.text = view.text
            }

            // В інших випадках дописуємо текст натиснутої
            // кнопки до тексту на screenTextView
            else {
                // Якщо на screenTextView відображений текст помилки,
                // замінюємо його на текст натиснутої кнопки
                if (displayingError)
                    screenTextView.text = view.text
                else
                    screenTextView.append(view.text)
                displayingError = false
                setScreenTextViewFontSize()
            }
        }
    }

    fun controlButtonClick(view: View) {
        // Якщо натиснутий елемент має тип AppCompatButton
        if (view is AppCompatButton) {

            // Якщо натиснута кнопка 'C' (clear)
            if (view.id == R.id.cancelButton) {
                // Якщо в тексті на screenTextView є лише '0'
                if (screenTextView.text.startsWith(getString(R.string.zero)) &&
                    screenTextView.text.length == 1
                ) {
                    // Повертаємо калькулятор у вихідний стан
                    operationType = OperationType.NONE
                    unhighlightOperatorButtons()
                }
                // Якщо в тексті на screenTextView є певне число
                else if (!screenTextView.text.equals(getString(R.string.zero))) {
                    // Стираємо число на screenTextView, і пишемо '0'
                    screenTextView.text = getString(R.string.zero)
                }
                displayingError = false
                setScreenTextViewFontSize()
            }

            // Якщо натиснута кнопка '+/-' (change sign)
            else if (view.id == R.id.changeSignButton) {
                // Якщо в тексті на screenTextView є лише '0', ігноруємо натискання
                if (screenTextView.text.startsWith(getString(R.string.zero)) &&
                    screenTextView.text.length == 1
                )
                    return
                // Якщо текст на screenTextView починається з '-', стираємо цей '-'
                if (screenTextView.text.startsWith('-'))
                    screenTextView.text = screenTextView.text.drop(1)
                // Якщо текст на screenTextView не починається з '-', дописуємо '-'
                else if (!screenTextView.text.startsWith('-'))
                    screenTextView.text = "-${screenTextView.text}"
            }

            // Якщо натиснута кнопка '%', обчислюємо [число на screenTextView] / 100
            else if (view.id == R.id.percentButton) {
                var value = screenTextView.text.toString().toDouble()
                value /= 100
                // Виводимо результат обчислення на screenTextView
                screenTextView.text = value.toString()
            }

            // Якщо натиснута кнопка '!' (factorial)
            else if (view.id == R.id.factorialButton) {
                // Якщо в тексті на screenTextView є '.', виводимо повідомлення про помилку
                if (screenTextView.text.contains(getString(R.string.point))) {
                    screenTextView.text = notInteger
                    displayingError = true
                    setScreenTextViewFontSize()
                    return
                }
                // Якщо [число на screenTextView] < 0, виводимо повідомлення про помилку
                if (screenTextView.text.startsWith('-')) {
                    screenTextView.text = lessThanZero
                    displayingError = true
                    setScreenTextViewFontSize()
                    return
                }
                // Обчислюємо факторіал [числа на screenTextView]
                val value = screenTextView.text.toString().toInt()
                var factorial: Long = 1
                for (i in 1..value)
                    factorial *= i
                // Виводимо результат обчислення на screenTextView
                screenTextView.text = factorial.toString()
            }

            // Якщо натиснута кнопка '√' (square_root)
            else if (view.id == R.id.rootButton) {
                // Якщо [число на screenTextView] < 0, виводимо повідомлення про помилку
                if (screenTextView.text.startsWith('-')) {
                    screenTextView.text = lessThanZero
                    displayingError = true
                    setScreenTextViewFontSize()
                    return
                }
                // Обчислюємо sqrt([числа на screenTextView])
                var value = screenTextView.text.toString().toDouble()
                value = sqrt(value)
                // Виводимо результат обчислення на screenTextView
                screenTextView.text = value.toString()
            }

            // Якщо натиснута кнопка 'π' (pi), в тексті на screenTextView пишемо Pi
            else if (view.id == R.id.piButton)
                screenTextView.text = PI.toString()
        }
    }

    fun operatorButtonClick(view: View) {
        // Якщо натиснутий елемент має тип AppCompatButton
        if (view is AppCompatButton) {

            // Якщо натиснута кнопка не є '='
            if (view.id != R.id.equalsButton) {
                // Якщо на screenTextView відображений текст помилки,
                // ігноруємо це натискання
                if (displayingError)
                    return
                // Якщо в калькуляторі ще не записаний оператор
                if (operationType == OperationType.NONE) {
                    // Зберігаємо в змінну A число з screenTextView
                    numberA = screenTextView.text.toString().toDouble()
                    // Стираємо число на screenTextView, і пишемо '0'
                    screenTextView.text = getString(R.string.zero)
                }
                // Записуємо в калькулятор тип викликаного оператора
                if (view.id == R.id.powerButton)
                    operationType = OperationType.POWER
                else if (view.id == R.id.divideButton)
                    operationType = OperationType.DIVIDE
                else if (view.id == R.id.multiplyButton)
                    operationType = OperationType.MULTIPLY
                else if (view.id == R.id.minusButton)
                    operationType = OperationType.MINUS
                else if (view.id == R.id.plusButton)
                    operationType = OperationType.PLUS
                // Виділяємо натиснуту кнопку оператора
                highlightOperatorButton(operationType)
            }

            // Якщо натиснута кнопка є '='
            else if (view.id == R.id.equalsButton) {
                // Якщо в калькуляторі не записаний оператор або на
                // screenTextView відображений текст помилки, ігноруємо натискання
                if (operationType == OperationType.NONE || displayingError)
                    return
                // Зберігаємо в змінну B число з screenTextView
                numberB = screenTextView.text.toString().toDouble()
                // Деактивуємо виділення кнопки оператора
                unhighlightOperatorButtons()
                // Якщо відбувається ділення на 0, виводимо повідомлення про помилку
                if (operationType == OperationType.DIVIDE && numberB == 0.0) {
                    screenTextView.text = divisionByZero
                    displayingError = true
                    setScreenTextViewFontSize()
                    // Видаляємо записаний оператор - калькулятор у вихідному стані
                    operationType = OperationType.NONE
                    return
                }
                // Обчислюємо результат відповідно до обраного оператора
                val operationResult: Double = when (operationType) {
                    OperationType.POWER -> numberA.pow(numberB)
                    OperationType.DIVIDE -> numberA / numberB
                    OperationType.MULTIPLY -> numberA * numberB
                    OperationType.MINUS -> numberA - numberB
                    OperationType.PLUS -> numberA + numberB
                    OperationType.NONE -> 0.0
                }
                // Записуємо змінні (A B), оператор та результат в консоль
                Log.d("console", "numberA = $numberA")
                Log.d("console", "operationType = $operationType")
                Log.d("console", "numberB = $numberB")
                Log.d("console", "operationResult = $operationResult")
                // Виводимо результат обчислення на screenTextView
                screenTextView.text = operationResult.toString()
                // Якщо результат є цілим числом, забираємо ".0" в кінці
                val subs = screenTextView.text.split(getString(R.string.point))
                if (subs.count() == 2) {
                    if (subs[1].startsWith(getString(R.string.zero)) && subs[1].length == 1)
                        screenTextView.text = subs[0]
                }
                // Видаляємо записаний оператор - калькулятор у вихідному стані
                operationType = OperationType.NONE
            }
        }
    }

    private fun highlightOperatorButton(operationType: OperationType) {
        // Деактивуємо виділення усіх кнопок операторів
        unhighlightOperatorButtons()

        // Знаходимо об'єкти кнопок операторів
        val powerButton: AppCompatButton = findViewById(R.id.powerButton)
        val divideButton: AppCompatButton = findViewById(R.id.divideButton)
        val multiplyButton: AppCompatButton = findViewById(R.id.multiplyButton)
        val minusButton: AppCompatButton = findViewById(R.id.minusButton)
        val plusButton: AppCompatButton = findViewById(R.id.plusButton)

        // Виділяємо кнопку викликаного оператора
        when (operationType) {
            OperationType.POWER -> {
                powerButton.setBackgroundColor(getColor(R.color.operator_button_highlighted))
                powerButton.setTextColor(getColor(R.color.black))
            }

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
        // Знаходимо об'єкти кнопок операторів
        val powerButton: AppCompatButton = findViewById(R.id.powerButton)
        val divideButton: AppCompatButton = findViewById(R.id.divideButton)
        val multiplyButton: AppCompatButton = findViewById(R.id.multiplyButton)
        val minusButton: AppCompatButton = findViewById(R.id.minusButton)
        val plusButton: AppCompatButton = findViewById(R.id.plusButton)

        // Деактивуємо виділення усіх кнопок операторів
        powerButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        powerButton.setTextColor(getColor(R.color.white))

        divideButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        divideButton.setTextColor(getColor(R.color.white))

        multiplyButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        multiplyButton.setTextColor(getColor(R.color.white))

        minusButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        minusButton.setTextColor(getColor(R.color.white))

        plusButton.setBackgroundColor(getColor(R.color.operator_button_normal))
        plusButton.setTextColor(getColor(R.color.white))
    }

    private fun setScreenTextViewFontSize() {
        if (displayingError)
            screenTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)
        else
            screenTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f)
    }
}
