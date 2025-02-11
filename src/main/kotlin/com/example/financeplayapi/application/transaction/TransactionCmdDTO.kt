package com.example.financeplayapi.application.transaction

import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.domain.WalletTransactionCmdDTO
import org.joda.time.DateTime

data class TransactionCmdDTO(
    val recipient: String,
    val description: String?,
    val totalValue: Money? = null,
    val categoryIds: List<Long>?,
    val walletIds: List<WalletTransactionCmdDTO>?,
    val transactionDatetime: DateTime? = null
)