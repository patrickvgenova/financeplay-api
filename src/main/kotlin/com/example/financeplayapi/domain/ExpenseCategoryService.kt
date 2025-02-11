package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.exception.AccessDeniedException
import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.core.exception.PaymentRequiredException
import com.example.financeplayapi.commons.spring.domain.AbstractDomainService
import com.example.financeplayapi.domain.entity.ExpenseCategory
import com.example.financeplayapi.domain.repository.ExpenseCategoryRepository
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExpenseCategoryService(
    private val expenseCategoryRepository: ExpenseCategoryRepository,
    private val userService: UserService,
) : AbstractDomainService() {

    @Value("\${finance-play.expense-category.limit.free}")
    private lateinit var limitFree: String

    @Transactional(TxType.REQUIRED)
    fun createExpenseCategoryForMe(
        userId: UUID,
        name: String,
        color: String,
    ): ExpenseCategory {
        val user = userService.findUser(userId)

        val checkExpenseCategory = expenseCategoryRepository.find(userId)
        if (user.plan == PlanType.FREE) {
            if (checkExpenseCategory.size >= limitFree.toInt()) {
                throw PaymentRequiredException("You have exceeded the creation limit allowed for your free account. To keep creating, upgrade to a paid account.")
            }
        }

        if (checkExpenseCategory.any { it.name?.lowercase() == name.lowercase() })
            throw IllegalArgumentException("There is already a category with that name")

        val expenseCategory = ExpenseCategory(
            name = name,
            color = color,
            user = user,
        )

        return expenseCategoryRepository.add(expenseCategory)
    }

    fun findExpenseCategories(
        userId: UUID,
        name: String?,
    ): List<ExpenseCategory> {
        return expenseCategoryRepository.find(userId, name)
    }

    fun findExpenseCategory(
        userId: UUID,
        expenseCategoryId: Long,
    ): ExpenseCategory {
        return expenseCategoryRepository.findById(userId, expenseCategoryId)
            ?: throw EntityNotFoundException("Expense category with id $expenseCategoryId not found")
    }

    @Transactional(TxType.REQUIRED)
    fun changeExpenseCategory(
        userId: UUID,
        id: Long,
        name: String?,
        color: String?,
    ): ExpenseCategory {
        val expenseCategory =
            expenseCategoryRepository.findById(id) ?: throw EntityNotFoundException("Expense Category not found")
        if (expenseCategory.user.idExternal != userId) throw AccessDeniedException("This user does not have permission on the wallet")
        return expenseCategory.updateFrom(name, color)
    }

    @Transactional(TxType.REQUIRED)
    fun deleteExpenseCategory(
        userId: UUID,
        id: Long,
    ) {
        val expenseCategory =
            expenseCategoryRepository.findById(id) ?: throw EntityNotFoundException("Expense Category not found")
        if (expenseCategory.user.idExternal != userId) throw AccessDeniedException("This user does not have permission on the wallet")
        return expenseCategoryRepository.delete(expenseCategory)
    }

}