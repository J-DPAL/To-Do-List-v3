package com.example.sa4

fun calculate(expr: String): String {
    return try {
        val tokens = tokenize(expr.replace("%", "/100"))  // Turn % into division
        val rpn = infixToPostfix(tokens)
        val result = evaluatePostfix(rpn)
        result.toString().removeSuffix(".0")
    } catch (e: Exception) {
        "Err"
    }
}

// Tokenize input into numbers, operators, parentheses
fun tokenize(expr: String): List<String> {
    val tokens = mutableListOf<String>()
    var current = ""
    for (ch in expr) {
        when (ch) {
            in "0123456789." -> current += ch
            in "+-*/()" -> {
                if (current.isNotEmpty()) {
                    tokens.add(current)
                    current = ""
                }
                tokens.add(ch.toString())
            }
            else -> return emptyList()
        }
    }
    if (current.isNotEmpty()) tokens.add(current)
    return tokens
}

// Convert infix expression to postfix (RPN)
fun infixToPostfix(tokens: List<String>): List<String> {
    val output = mutableListOf<String>()
    val stack = ArrayDeque<String>()
    val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

    for (token in tokens) {
        when {
            token.toDoubleOrNull() != null -> output.add(token)
            token == "(" -> stack.addFirst(token)
            token == ")" -> {
                while (stack.isNotEmpty() && stack.first() != "(")
                    output.add(stack.removeFirst())
                stack.removeFirstOrNull()  // Remove "("
            }
            token in precedence -> {
                while (stack.isNotEmpty() && stack.first() != "(" &&
                    precedence[stack.first()]!! >= precedence[token]!!) {
                    output.add(stack.removeFirst())
                }
                stack.addFirst(token)
            }
        }
    }
    while (stack.isNotEmpty()) {
        output.add(stack.removeFirst())
    }
    return output
}

// Evaluate postfix expression
fun evaluatePostfix(tokens: List<String>): Double {
    val stack = ArrayDeque<Double>()
    for (token in tokens) {
        when {
            token.toDoubleOrNull() != null -> stack.addFirst(token.toDouble())
            else -> {
                val b = stack.removeFirst()
                val a = stack.removeFirst()
                val result = when (token) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> if (b == 0.0) throw ArithmeticException() else a / b
                    else -> throw IllegalArgumentException("Unknown op: $token")
                }
                stack.addFirst(result)
            }
        }
    }
    return stack.first()
}
