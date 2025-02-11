package com.example.financeplayapi.commons

import java.text.Normalizer

fun String.normalizeAndLowercase(): String {
    val str = Normalizer.normalize(this, Normalizer.Form.NFD)
    return str.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        .replace("\\p{M}".toRegex(), "").lowercase()
}