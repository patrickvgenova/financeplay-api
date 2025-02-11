package com.example.financeplayapi.application.transaction

import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.core.exception.IllegalArgumentException
import com.example.financeplayapi.commons.spring.application.PageDTO
import com.example.financeplayapi.commons.spring.context.RequestContext
import com.example.financeplayapi.domain.*
import org.joda.time.LocalDate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/transaction/{transactionType}")
class TransactionController(
    private val expenseTransactionService: ExpenseService,
    private val revenueTransactionService: RevenueService,
    private val requestContext: RequestContext,
) {

    @PostMapping
    fun createTransaction(
        @PathVariable transactionType: TransactionType,
        @RequestBody transactionDto: TransactionCmdDTO,
    ): ResponseEntity<TransactionQueryDTO> {
        val transaction = if (transactionType === TransactionType.REVENUE) {
            revenueTransactionService.createRevenueForMe(
                userId = requestContext.userId,
                recipient = transactionDto.recipient,
                description = transactionDto.description,
                totalValue = transactionDto.totalValue,
                revenueCategoryId = transactionDto.categoryIds,
                walletIds = transactionDto.walletIds,
                transactionDatetime = transactionDto.transactionDatetime
            )
        } else if (transactionType === TransactionType.EXPENSE) {
            expenseTransactionService.createExpenseForMe(
                userId = requestContext.userId,
                recipient = transactionDto.recipient,
                description = transactionDto.description,
                totalValue = transactionDto.totalValue,
                expenseCategoryId = transactionDto.categoryIds,
                walletIds = transactionDto.walletIds,
                transactionDatetime = transactionDto.transactionDatetime
            )
        } else throw EntityNotFoundException("Transaction type not found")
        return ResponseEntity.ok(TransactionQueryDTO(transaction))
    }

    //    @RequiresPermission("FREE")
    @GetMapping
    fun getTransactions(
        @PathVariable transactionType: TransactionType,
        @RequestParam("startTransactionDate") startTransactionDate: LocalDate?,
        @RequestParam("endTransactionDate") endTransactionDate: LocalDate?,
        @RequestParam("searchRecipient") searchRecipient: String?,
        @RequestParam("searchDescription") searchDescription: String?,
        @RequestParam("categoryIds") categoryIds: List<Long>?,
        @RequestParam("walletIds") walletIds: List<Long>?,
        @RequestParam("page") page: Int? = null,
        @RequestParam("offset") offset: Int? = null,
        @RequestParam("size") size: Int? = null,
        @RequestParam("sort") sort: String? = null,
    ): ResponseEntity<PageDTO<TransactionQueryDTO>> {
        val transaction = if (transactionType === TransactionType.REVENUE) {
            revenueTransactionService.getMyRevenues(
                requestContext.userId,
                startTransactionDate = startTransactionDate,
                endTransactionDate = endTransactionDate,
                searchRecipient = searchRecipient,
                searchDescription = searchDescription,
                categoryIds = categoryIds,
                walletIds = walletIds,
                )
        } else if (transactionType === TransactionType.EXPENSE) {
            expenseTransactionService.getMyExpenses(
                requestContext.userId,
                startTransactionDate = startTransactionDate,
                endTransactionDate = endTransactionDate,
                searchRecipient = searchRecipient,
                searchDescription = searchDescription,
                categoryIds = categoryIds,
                walletIds = walletIds,
                )
        } else throw EntityNotFoundException("Transaction type not found")
        return ResponseEntity.ok(transaction.convert { TransactionQueryDTO(it) })
    }

    @GetMapping("/{transactionId}")
    fun getTransaction(
        @PathVariable transactionType: TransactionType,
        @PathVariable transactionId: UUID,
    ): ResponseEntity<TransactionQueryDTO> {
        val transaction = if (transactionType === TransactionType.REVENUE) {
            revenueTransactionService.getMyRevenue(requestContext.userId, transactionId)
        } else if (transactionType === TransactionType.EXPENSE) {
            expenseTransactionService.getMyExpense(requestContext.userId, transactionId)
        } else throw EntityNotFoundException("Transaction type not found")
        return ResponseEntity.ok(TransactionQueryDTO(transaction))
    }


    @PutMapping("/{transactionId}")
    fun changeTransaction(
        @PathVariable transactionType: TransactionType,
        @PathVariable transactionId: UUID,
        @RequestBody transactionDto: TransactionCmdDTO,
    ): ResponseEntity<TransactionQueryDTO> {
        val transaction = if (transactionType === TransactionType.REVENUE) {
            revenueTransactionService.changeMyRevenue(
                userId = requestContext.userId,
                revenueId = transactionId,
                recipient = transactionDto.recipient,
                description = transactionDto.description,
                totalValue = transactionDto.totalValue,
                revenueCategoryId = transactionDto.categoryIds,
                walletIds = transactionDto.walletIds,
                transactionDatetime = transactionDto.transactionDatetime
            )
        } else if (transactionType === TransactionType.EXPENSE) {
            expenseTransactionService.changeMyExpense(
                userId = requestContext.userId,
                expenseId = transactionId,
                recipient = transactionDto.recipient,
                description = transactionDto.description,
                totalValue = transactionDto.totalValue,
                expenseCategoryId = transactionDto.categoryIds,
                walletIds = transactionDto.walletIds,
                transactionDatetime = transactionDto.transactionDatetime
            )
        } else throw EntityNotFoundException("Transaction type not found")
        return ResponseEntity.ok(TransactionQueryDTO(transaction))
    }

    @DeleteMapping("/{transactionId}")
    fun deleteTransaction(
        @PathVariable transactionType: TransactionType,
        @PathVariable transactionId: UUID,
    ): ResponseEntity<Unit> {
        val transaction = if (transactionType === TransactionType.REVENUE) {
            revenueTransactionService.deleteMyRevenue(requestContext.userId, transactionId)
        } else if (transactionType === TransactionType.EXPENSE) {
            expenseTransactionService.deleteMyExpense(requestContext.userId, transactionId)
        } else throw EntityNotFoundException("Transaction type not found")
        return ResponseEntity.ok().build()
    }
}