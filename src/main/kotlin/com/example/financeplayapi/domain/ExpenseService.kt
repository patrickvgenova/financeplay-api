package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.commons.spring.application.PageDTO
import com.example.financeplayapi.commons.spring.domain.AbstractDomainService
import com.example.financeplayapi.domain.entity.Expense
import com.example.financeplayapi.domain.entity.WalletTransaction
import com.example.financeplayapi.domain.repository.*
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
    private val walletTransactionRepository: WalletTransactionRepository,
    private val expenseCategoryRepository: ExpenseCategoryRepository,
) : AbstractDomainService() {

    @Transactional(TxType.REQUIRED)
    fun createExpenseForMe(
        userId: UUID,
        recipient: String,
        description: String?,
        totalValue: Money? = null,
        expenseCategoryId: List<Long>?,
        walletIds: List<WalletTransactionCmdDTO>?,
        transactionDatetime: DateTime? = null,
    ): Expense {
        val user = userRepository.findByExternalId(userId) ?: throw IllegalArgumentException("User not found")
        val expenseCategory = expenseCategoryId?.let { expenseCategoryRepository.findByIds(userId, it) }

        val expense = expenseRepository.add(
            Expense(
                user = user,
                recipient = recipient,
                description = description,
                totalValue = totalValue,
                category = expenseCategory?.first(), // FIXME: Arrumar
                transactionDatetime = transactionDatetime ?: DateTime.now(),
            )
        )

        if (!walletIds.isNullOrEmpty()) {
            val wallets = walletRepository.findByIds(userId, walletIds.map { it.walletId })
            wallets.forEach {
                walletTransactionRepository.add(
                    WalletTransaction(
//                        user = user,
                        transaction = expense,
                        wallet = it,
                        value = walletIds.first { w -> w.walletId == it.id }.value
                    )
                )
            }
        }

        return expense
    }

    fun getMyExpenses(
        userId: UUID,
        startTransactionDate: LocalDate?,
        endTransactionDate: LocalDate?,
        searchRecipient: String?,
        searchDescription: String?,
        categoryIds: List<Long>?,
        walletIds: List<Long>?,
        page: Int? = null,
        offset: Int? = null,
        size: Int? = null,
        sort: String? = null,
    ): PageDTO<Expense> {
        val expenses = expenseRepository.find(
            userId = userId,
            minTransactionDatetime = startTransactionDate?.toDateTimeAtStartOfDay(),
            maxTransactionDatetime = endTransactionDate?.toDateTimeAtStartOfDay()?.plusDays(1),
            recipient = searchRecipient,
            description = searchDescription,
            expenseCategoryIds = categoryIds,
            walletIds = walletIds,
            pageable = getPage(page, offset, size, sort)
        )
        return PageDTO(expenses)
    }

    fun getMyExpense(
        userId: UUID,
        expenseId: UUID,
    ): Expense {
        val expense = expenseRepository.findByExternalId(
            userId = userId,
            expenseId = expenseId
        ) ?: throw EntityNotFoundException("Expense not found")
        return expense
    }

    @Transactional(TxType.REQUIRED)
    fun changeMyExpense(
        userId: UUID,
        expenseId: UUID,
        recipient: String? = null,
        description: String? = null,
        totalValue: Money? = null,
        expenseCategoryId: List<Long>?,
        walletIds: List<WalletTransactionCmdDTO>? = null,
        transactionDatetime: DateTime? = null,
    ): Expense {
        val expense = expenseRepository.findByExternalId(
            userId = userId,
            expenseId = expenseId
        ) ?: throw EntityNotFoundException("Expense not found")

        val expenseCategory = expenseCategoryId?.let { expenseCategoryRepository.findByIds(userId, it) }

        return expense.update(
            recipient = recipient,
            description = description,
            totalValue = totalValue,
            expenseCategory = expenseCategory?.first(),
            transactionDatetime = transactionDatetime
        )
    }

    @Transactional(TxType.REQUIRED)
    fun deleteMyExpense(
        userId: UUID,
        expenseId: UUID,
    ) {
        val expense = expenseRepository.findByExternalId(
            userId = userId,
            expenseId = expenseId
        ) ?: throw EntityNotFoundException("Expense not found")
        expenseRepository.delete(expense)
    }
}