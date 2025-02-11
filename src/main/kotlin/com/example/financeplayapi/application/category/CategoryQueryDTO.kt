package com.example.financeplayapi.application.category

import com.example.financeplayapi.domain.CategoryType
import com.example.financeplayapi.domain.entity.AbstractCategory
import com.example.financeplayapi.domain.entity.RevenueCategory

data class CategoryQueryDTO(
    val id: Long,
    val name: String,
    val color: String?,
    val type: CategoryType
) {
    constructor(entity: AbstractCategory) : this(
        id = entity.id!!,
        name = entity.name,
        color = entity.color,
        type = if (entity is RevenueCategory) CategoryType.REVENUE else CategoryType.EXPENSE
    )
}