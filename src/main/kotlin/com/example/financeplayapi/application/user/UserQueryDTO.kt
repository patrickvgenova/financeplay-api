package com.example.financeplayapi.application.user

import com.example.financeplayapi.domain.PlanType
import com.example.financeplayapi.domain.entity.User
import java.util.*

data class UserQueryDTO(
    val idExternal: UUID,
    val name: String?,
    val email: String,
    val photoUrl: String?,
    val document: String?,
    val phoneNumber: String?,
    val planType: PlanType,
    val registrationCompleted: Boolean
) {
    constructor(entity: User): this(
        idExternal = entity.idExternal,
        name = entity.name,
        email = entity.email,
        photoUrl = entity.photoUrl,
        document = entity.document,
        phoneNumber = entity.phoneNumber,
        planType = entity.plan,
        registrationCompleted = entity.name.isNullOrEmpty()
    )
}