package com.example.sa4

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorKtUnitTest {

    @Test fun addition_works() {
        val result = calculate("2+3")
        assertEquals("5", result)
    }

    @Test fun division_by_zero_showsErr() {
        val result = calculate("1/0")
        assertEquals("Err", result)
    }

    @Test fun decimal_input_works() {
        val result = calculate("2.5+0.5")
        assertEquals("3", result)  // Because .removeSuffix(".0") is applied
    }
}
