package com.samuel.finances.util

import java.text.NumberFormat
import java.util.Locale

fun Double.toBrazilianCurrency(): String {
    return NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .format(this)
}