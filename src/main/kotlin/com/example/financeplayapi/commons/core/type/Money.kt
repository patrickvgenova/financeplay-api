package com.example.financeplayapi.commons.core.type

class Money(amount: Number) : Numeric(amount, 2), Comparable<Money> {

    fun multiply(rate: Rate): Money {
        return Money(super.innerMultiply(rate))
    }

    fun divide(number: Number): Money {
        return div(number)
    }

    fun divide(number: Money): Rate {
        return div(number)
    }

    operator fun div(number: Number): Money {
        return Money(super.innerDivide(number.toDouble().toBigDecimal()))
    }

    operator fun div(number: Money): Rate {
        return Rate(super.innerDivide(number))
    }

    operator fun times(tax: Rate): Money {
        return this.multiply(tax)
    }

    operator fun times(number: Number): Money {
        return Money(this.innerMultiply(number.toDouble().toBigDecimal()))
    }

    operator fun plus(money: Money): Money {
        return Money(super.innerPlus(money))
    }

    operator fun minus(money: Money): Money {
        return Money(super.innerMinus(money))
    }

    override fun compareTo(other: Money): Int {
        return this.toBigDecimal().compareTo(other.toBigDecimal())
    }

    override fun toString(): String {
        return super.toBigDecimal().toString()
    }

    companion object {
        val ZERO = Money(0)
        val ONE = Money(1)
    }
}

fun max(money1: Money, money2: Money): Money {
    return if (money1 > money2) money1 else money2
}

fun min(money1: Money, money2: Money): Money {
    return if (money1 < money2) money1 else money2
}
