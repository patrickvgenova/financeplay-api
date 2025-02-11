package com.example.financeplayapi.application.transaction

import com.example.financeplayapi.application.category.CategoryQueryDTO
import com.example.financeplayapi.application.wallet.WalletQueryDTO
import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.domain.CategoryType
import com.example.financeplayapi.domain.entity.AbstractTransaction
import com.example.financeplayapi.domain.entity.Revenue
import org.joda.time.DateTime
import java.util.UUID

data class TransactionQueryDTO(
    val id: UUID,
    val recipient: String,
    val description: String?,
    val totalValue: Money? = null,
    val category: CategoryQueryDTO?,
    val wallet: List<WalletQueryDTO>,
    val transactionDatetime: DateTime? = null,
    val type: CategoryType
) {
    constructor(entity: AbstractTransaction) : this(
        id = entity.idExternal,
        recipient = entity.recipient!!,
        description = entity.description,
        totalValue = entity.totalValue,
        category = entity.category?.let { CategoryQueryDTO(it) },
        wallet = entity.wallets.map { WalletQueryDTO(it.wallet) },
        transactionDatetime = entity.transactionDatetime ?: DateTime.now(),
        type = if (entity is Revenue) CategoryType.REVENUE else CategoryType.EXPENSE
    )
}