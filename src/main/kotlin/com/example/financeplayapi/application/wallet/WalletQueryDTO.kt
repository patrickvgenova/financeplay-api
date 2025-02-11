package com.example.financeplayapi.application.wallet

import com.example.financeplayapi.domain.entity.Wallet

data class WalletQueryDTO(
    val id: Long,
    val name: String,
    val color: String?,
) {
    constructor(entity: Wallet) : this(
        id = entity.id!!,
        name = entity.name,
        color = entity.color,
    )
}