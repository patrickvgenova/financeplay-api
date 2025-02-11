package com.example.financeplayapi.domain.entity

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue(value = "REVENUE")
open class RevenueCategory(
    name: String,
    color: String,
    user: User,
): AbstractCategory(
    name = name,
    color = color,
    user = user
) {

    fun updateFrom(
        name: String?,
        color: String?,
    ): RevenueCategory {
        name?.let { this.name = it }
        color?.let { this.color = it }
        return this
    }

    companion object {
        const val USER__USER = "user"
        const val NAME__STRING = "name"
        const val ID__LONG = "id"
    }
}