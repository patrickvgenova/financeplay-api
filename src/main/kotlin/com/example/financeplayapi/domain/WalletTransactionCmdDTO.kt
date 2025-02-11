package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.type.Money
import java.util.UUID

data class WalletTransactionCmdDTO(
    val walletId: Long,
    val transactionId: UUID,
    val value: Money

)
