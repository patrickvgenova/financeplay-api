package com.example.financeplayapi.application.user

import com.example.financeplayapi.domain.PlanType
import org.springframework.web.multipart.MultipartFile

data class UserCmdDTO(
    val name: String?,
    val photoUrl: String?,
    val photo: MultipartFile?,
    val document: String?,
    val phoneNumber: String?,
    val planType: PlanType?
)