package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.core.type.Money
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import org.joda.time.DateTime

@Entity
@DiscriminatorValue(value = "EXPENSE")
open class Expense(
    recipient: String?,
    description: String?,
    totalValue: Money?,
    user: User,
    category: ExpenseCategory?,
    transactionDatetime: DateTime?,
): AbstractTransaction(
    recipient = recipient,
    description = description,
    totalValue = totalValue,
    user = user,
    category = category,
    transactionDatetime = transactionDatetime,
) {

    fun update(
        recipient: String? = null,
        description: String? = null,
        totalValue: Money? = null,
        expenseCategory: ExpenseCategory? = null,
        transactionDatetime: DateTime? = null,
    ): Expense {
        val ret = (this.recipient != recipient) ||
                (this.description != description) ||
                (this.totalValue != totalValue) ||
                (this.category != expenseCategory) ||
                (this.transactionDatetime != transactionDatetime)

        recipient?.let { this.recipient = it }
        description?.let { this.description = it }
        totalValue?.let { this.totalValue = it }
        expenseCategory?.let { this.category = it }
        transactionDatetime?.let { this.transactionDatetime = it }

//        if (ret) this.updatedAt = DateTime.now()

        return this
    }

    companion object {
        const val USER__USER = "user"
        const val RECIPIENT__STRING = "recipient"
        const val DESCRIPTION__STRING = "description"
        const val WALLETS__LIST_WALLET = "wallets"
        const val CATEGORY__CATEGORY = "category"
        const val TRANSACTION_DATETIME__DATETIME = "transactionDatetime"
        const val ID_EXTERNAL__UUID = AbstractTransaction.ID_EXTERNAL__UUID
    }
}