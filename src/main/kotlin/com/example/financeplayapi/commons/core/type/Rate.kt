package com.example.financeplayapi.commons.core.type

class Rate(percent: Number) : Numeric(percent, 4), Comparable<Rate> {

    operator fun times(money: Money): Money {
        return this.multiply(money)
    }

    operator fun times(tax: Rate): Rate {
        return this.multiply(tax)
    }

    fun multiply(money: Money): Money {
        return Money(super.innerMultiply(money))
    }

    fun multiply(rate: Rate): Rate {
        return Rate(super.innerMultiply(rate))
    }

    operator fun plus(rate: Rate): Rate {
        return Rate(super.innerPlus(rate))
    }

    operator fun minus(rate: Rate): Rate {
        return Rate(super.innerMinus(rate))
    }

    override fun compareTo(other: Rate): Int {
        return this.toBigDecimal().compareTo(other.toBigDecimal())
    }

    override fun toString(): String {
        return super.toBigDecimal().toString()
    }

    companion object {
        val ZERO = Rate(0)
        val HALF = Rate(0.005)
        val ONE = Rate(0.01)
        val _5 = Rate(0.05)
        val _10 = Rate(0.1)
        val _20 = Rate(0.2)
        val _25 = Rate(0.25)
        val _30 = Rate(0.3)
        val _40 = Rate(0.4)
        val _50 = Rate(0.5)
        val _60 = Rate(0.6)
        val _70 = Rate(0.7)
        val _75 = Rate(0.75)
        val _80 = Rate(0.8)
        val _90 = Rate(.9)
        val _100 = Rate(1)
    }
}

fun max(rate1: Rate, rate2: Rate): Rate {
    return if (rate1 > rate2) rate1 else rate2
}

fun min(rate1: Rate, rate2: Rate): Rate {
    return if (rate1 < rate2) rate1 else rate2
}
