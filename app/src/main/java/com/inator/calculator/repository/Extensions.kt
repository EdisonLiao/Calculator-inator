package com.inator.calculator.repository

import java.math.BigDecimal

fun String.toExpression(): String {
    return replace('×', '*')
        .replace('÷', '/')
        .replace("π", "pi")
        .replace("sin⁻¹", "asin")
        .replace("cos⁻¹", "acos")
        .replace("tan⁻¹", "atan")
        .replace("log", "log10")
        .maybeAppendClosedBrackets()
}

fun Double.toSimpleString(): String {
    var result = toString()
    result.indexOf(".0").let {
        if (it != -1) {
            result = result.substring(0, it)
        }
    }
    return result
}

fun BigDecimal.toSimpleString(): String {
    var result = toPlainString()
    result.indexOf(".0").let {
        if (it != -1) {
            result = result.substring(0, it)
        }
    }
    return result
}

private fun String.maybeAppendClosedBrackets(): String {
    var expression = this

    var open = 0
    var close = 0

    for (i in 0 until length) {
        if (this[i] == '(') {
            open++
        } else if (this[i] == ')') {
            close++
        }
    }
    if (open - close > 0) {
        for (j in 0 until open - close) expression += ")"
    } else if (close - open > 0) {
        for (j in 0 until close - open) expression = "($expression"
    }

    return expression
}

