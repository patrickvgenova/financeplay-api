package com.example.financeplayapi.commons.core.type

import java.math.BigDecimal
import java.math.RoundingMode

abstract class Numeric private constructor(private val number: BigDecimal, private val precision: Int) : Number() {

    constructor(number: Number, precision: Int) : this(from(number.toDouble()), precision)

    fun toBigDecimal(): BigDecimal {
        return this.number.setScale(precision, RoundingMode.HALF_EVEN)
    }

    fun isPositive(): Boolean {
        return this.number > BigDecimal.ZERO
    }

    fun isNegative(): Boolean {
        return this.number < BigDecimal.ZERO
    }

    fun isZero(): Boolean {
        return this.number.compareTo(BigDecimal.ZERO) == 0
    }

    protected fun innerPlus(numeric: Numeric): BigDecimal {
        return this.number.plus(numeric.number)
    }

    protected fun innerMultiply(numeric: Numeric): BigDecimal {
        return this.number.multiply(numeric.number).setScale(CALCULATION_PRECISION, RoundingMode.HALF_EVEN)
    }

    protected fun innerMultiply(numeric: BigDecimal): BigDecimal {
        return this.number.multiply(numeric).setScale(CALCULATION_PRECISION, RoundingMode.HALF_EVEN)
    }

    protected fun innerDivide(number: BigDecimal): BigDecimal {
        return this.number.divide(number, CALCULATION_PRECISION, RoundingMode.HALF_EVEN)
    }

    protected fun innerDivide(number: Numeric): BigDecimal {
        return this.number.divide(number.number, CALCULATION_PRECISION, RoundingMode.HALF_EVEN)
    }

    protected fun innerMinus(numeric: Numeric): BigDecimal {
        return this.number.minus(numeric.number)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Numeric

        return toBigDecimal() == other.toBigDecimal()
    }

    override fun hashCode(): Int {
        return number.hashCode()
    }

    open operator fun times(n: Numeric): Numeric {
        if (this is Money && n is Rate) {
            return this.multiply(n)
        }
        if (this is Rate) {
            if (n is Money) {
                return this.multiply(n)
            } else if (n is Rate) {
                return this.multiply(n)
            }
        }
        throw UnsupportedOperationException()
    }

    override fun toByte(): Byte {
        return toBigDecimal().toByte()
    }

    override fun toChar(): Char {
        return toBigDecimal().toChar()
    }

    override fun toDouble(): Double {
        return toBigDecimal().toDouble()
    }

    override fun toFloat(): Float {
        return toBigDecimal().toFloat()
    }

    override fun toInt(): Int {
        return toBigDecimal().toInt()
    }

    override fun toLong(): Long {
        return toBigDecimal().toLong()
    }

    override fun toShort(): Short {
        return toBigDecimal().toShort()
    }

    companion object {

        private const val CALCULATION_PRECISION: Int = 12

        private fun from(double: Double): BigDecimal {
            return standard(BigDecimal(double))
        }

        private fun standard(b: BigDecimal): BigDecimal {
            b.setScale(CALCULATION_PRECISION, RoundingMode.HALF_EVEN)
            return b
        }
    }
}
