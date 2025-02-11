package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.util.emptyUUID
import com.example.financeplayapi.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserPermissionService(
    private val userRepository: UserRepository
) {

    fun hasPermission(userId: UUID, permission: PlanType): Boolean {
        // TODO: Implementar gestão de permissões
        val user = userRepository.findByExternalId(userId) ?: return false
        return user.plan == permission
    }
}