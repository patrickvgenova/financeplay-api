package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.core.type.Money
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import org.joda.time.DateTime

@Entity
@DiscriminatorValue(value = "REVENUE")
open class Revenue(
    recipient: String?,
    description: String?,
    totalValue: Money?,
    user: User,
    revenueCategory: RevenueCategory?,
    transactionDatetime: DateTime?,
): AbstractTransaction(
    recipient = recipient,
    description = description,
    totalValue = totalValue,
    user = user,
    category = revenueCategory,
    transactionDatetime = transactionDatetime,
) {
    
    fun update(
        recipient: String? = null, 
        description: String? = null, 
        totalValue: Money? = null, 
        revenueCategory: RevenueCategory? = null, 
        transactionDatetime: DateTime? = null,
        ): Revenue {
        val ret = (this.recipient != recipient) ||
           (this.description != description) ||
           (this.totalValue != totalValue) ||
           (this.category != revenueCategory) ||
           (this.transactionDatetime != transactionDatetime)

       recipient?.let { this.recipient = it }
       description?.let { this.description = it }
       totalValue?.let { this.totalValue = it }
       revenueCategory?.let { this.category = it }
       transactionDatetime?.let { this.transactionDatetime = it }

//        if (ret) this.updatedAt = DateTime.now()

        return this
    }
    
    companion object {
        const val USER__USER = "user"
        const val RECIPIENT__STRING = "recipient"
        const val DESCRIPTION__STRING = "description"
//        const val QUANTITY__DOUBLE = "quantity"
//        const val UNIT_VALUE__MONEY = "unitValue"
//        const val TOTAL_VALUE__MONEY = "totalValue"
        const val CATEGORY__CATEGORY = "category"
        const val WALLETS__LIST_WALLET = "wallet"
        const val TRANSACTION_DATETIME__DATETIME = "transactionDatetime"
        const val ID_EXTERNAL__UUID = AbstractTransaction.ID_EXTERNAL__UUID
    }
}