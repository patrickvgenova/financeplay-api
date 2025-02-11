package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.exception.AccessDeniedException
import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.core.exception.PaymentRequiredException
import com.example.financeplayapi.commons.spring.domain.AbstractDomainService
import com.example.financeplayapi.domain.entity.RevenueCategory
import com.example.financeplayapi.domain.repository.RevenueCategoryRepository
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class RevenueCategoryService(
    private val revenueCategoryRepository: RevenueCategoryRepository,
    private val userService: UserService,
) : AbstractDomainService() {

    @Value("\${finance-play.revenue-category.limit.free}")
    private lateinit var limitFree: String

    @Transactional(TxType.REQUIRED)
    fun createRevenueCategory(
        userId: UUID,
        name: String,
        color: String,
    ): RevenueCategory {
        val user = userService.findUser(userId)

        val checkRevenueCategory = revenueCategoryRepository.find(userId)
        if (user.plan == PlanType.FREE) {
            if (checkRevenueCategory.size >= limitFree.toInt()) {
                throw PaymentRequiredException(
                    "You have exceeded the creation limit allowed for your free account. " +
                            "To keep creating, upgrade to a paid account."
                )
            }
        }

        if (checkRevenueCategory.any { it.name.lowercase() == name.lowercase() })
            throw IllegalArgumentException("There is already a category with that name")

        val revenueCategory = RevenueCategory(
            name = name,
            color = color,
            user = user,
        )

        return revenueCategoryRepository.add(revenueCategory)
    }

    fun findRevenueCategory(
        userId: UUID,
        revenueCategoryId: Long,
    ): RevenueCategory {
        return revenueCategoryRepository.findById(userId, revenueCategoryId)
            ?: throw EntityNotFoundException("Revenue category with id $revenueCategoryId not found")
    }

    fun findRevenueCategories(
        userId: UUID,
        name: String?,
    ): List<RevenueCategory> {
        return revenueCategoryRepository.find(userId, name)
    }

    @Transactional(TxType.REQUIRED)
    fun changeRevenueCategory(
        userId: UUID,
        id: Long,
        name: String?,
        color: String?,
    ): RevenueCategory {
        val revenueCategory =
            revenueCategoryRepository.findById(id) ?: throw EntityNotFoundException("Revenue Category not found")
        if (revenueCategory.user.idExternal != userId)
            throw AccessDeniedException("This user does not have permission on the wallet")
        return revenueCategory.updateFrom(name, color)
    }

    @Transactional(TxType.REQUIRED)
    fun deleteRevenueCategory(
        userId: UUID,
        id: Long,
    ) {
        val revenueCategory =
            revenueCategoryRepository.findById(id) ?: throw EntityNotFoundException("Revenue Category not found")
        if (revenueCategory.user.idExternal != userId)
            throw AccessDeniedException("This user does not have permission on the wallet")
        return revenueCategoryRepository.delete(revenueCategory)
    }

}