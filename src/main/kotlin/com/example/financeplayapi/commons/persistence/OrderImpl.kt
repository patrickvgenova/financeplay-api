package com.example.financeplayapi.commons.persistence


import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Order
import java.io.Serializable

class OrderImpl(
    expression: Expression<*>,
    ascending: Boolean,
    val nullsFirst: Boolean?
) : Order, Serializable {
    private val expression: Expression<*> = expression
    private val ascending: Boolean = ascending

    constructor(expression: Expression<*>) : this(expression, true, null as Boolean?)

    constructor(expression: Expression<*>, ascending: Boolean) : this(expression, ascending, null as Boolean?)

    override fun isAscending(): Boolean {
        return this.ascending
    }

    override fun reverse(): Order {
        return OrderImpl(this.expression, !this.ascending)
    }

    override fun getExpression(): Expression<*> {
        return this.expression
    }
}
