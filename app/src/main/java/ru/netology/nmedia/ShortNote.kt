package ru.netology.nmedia

import java.math.BigDecimal
import java.math.RoundingMode

fun shortNote(int: Int): String {

    val temp: BigDecimal = int.toBigDecimal().divide(1_000.toBigDecimal())
    val temp1: BigDecimal = int.toBigDecimal().divide(1_000.toBigDecimal())
    val temp2: BigDecimal = int.toBigDecimal().divide(1_000_000.toBigDecimal())

    return when (int) {
        in 1..999 -> int.toString()
        in 1_000..9_999 -> String.format("%.1f", temp.setScale(1, RoundingMode.FLOOR)) + "K"
        in 10_000..999_999 -> String.format("%.0f", temp1.setScale(0, RoundingMode.FLOOR)) + "K"
        else -> {
            String.format("%.1f", temp2.setScale(1, RoundingMode.FLOOR)) + "M"
        }
    }
}