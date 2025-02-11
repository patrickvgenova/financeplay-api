package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.commons.spring.application.PageDTO
import com.example.financeplayapi.commons.spring.domain.AbstractDomainService
import com.example.financeplayapi.domain.entity.Revenue
import com.example.financeplayapi.domain.entity.WalletTransaction
import com.example.financeplayapi.domain.repository.RevenueCategoryRepository
import com.example.financeplayapi.domain.repository.RevenueRepository
import com.example.financeplayapi.domain.repository.UserRepository
import com.example.financeplayapi.domain.repository.WalletRepository
import com.example.financeplayapi.domain.repository.WalletTransactionRepository
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RevenueService(
    private val revenueRepository: RevenueRepository,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
    private val walletTransactionRepository: WalletTransactionRepository,
    private val revenueCategoryRepository: RevenueCategoryRepository,
) : AbstractDomainService() {

    @Transactional(TxType.REQUIRED)
    fun createRevenueForMe(
        userId: UUID,
        recipient: String,
        description: String?,
        totalValue: Money? = null,
        revenueCategoryId: List<Long>?,
        walletIds: List<WalletTransactionCmdDTO>?,
        transactionDatetime: DateTime? = null,
    ): Revenue {
        val user = userRepository.findByExternalId(userId) ?: throw IllegalArgumentException("User not found")
        val revenueCategory = revenueCategoryId?.let { revenueCategoryRepository.findByIds(userId, it) }

        val revenue = revenueRepository.add(
            Revenue(
                user = user,
                recipient = recipient,
                description = description,
                totalValue = totalValue,
                revenueCategory = revenueCategory?.first(), // FIXME: Arrumar
                transactionDatetime = transactionDatetime ?: DateTime.now(),
            )
        )

        if (!walletIds.isNullOrEmpty()) {
            val wallets = walletRepository.findByIds(userId, walletIds.map { it.walletId })
            wallets.forEach {
                walletTransactionRepository.add(
                    WalletTransaction(
//                        user = user,
                        transaction = revenue,
                        wallet = it,
                        value = walletIds.first { w -> w.walletId == it.id }.value
                    )
                )
            }
        }

        return revenue
    }

    fun getMyRevenues(
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
    ): PageDTO<Revenue> {
        val revenues = revenueRepository.find(
            userId = userId,
            minTransactionDatetime = startTransactionDate?.toDateTimeAtStartOfDay(),
            maxTransactionDatetime = endTransactionDate?.toDateTimeAtStartOfDay()?.plusDays(1),
            recipient = searchRecipient,
            description = searchDescription,
            revenueCategoryIds = categoryIds,
            walletIds = walletIds,
            pageable = getPage(page, offset, size, sort)
        )
        return PageDTO(revenues)
    }

    fun getMyRevenue(
        userId: UUID,
        revenueId: UUID,
    ): Revenue {
        val revenue = revenueRepository.findByExternalId(
            userId = userId,
            revenueId = revenueId
        ) ?: throw EntityNotFoundException("Revenue not found")
        return revenue
    }

    @Transactional(TxType.REQUIRED)
    fun changeMyRevenue(
        userId: UUID,
        revenueId: UUID,
        recipient: String? = null,
        description: String? = null,
        totalValue: Money? = null,
        revenueCategoryId: List<Long>?,
        walletIds: List<WalletTransactionCmdDTO>? = null,
        transactionDatetime: DateTime? = null,
    ): Revenue {
        val revenue = revenueRepository.findByExternalId(
            userId = userId,
            revenueId = revenueId
        ) ?: throw EntityNotFoundException("Revenue not found")

        val revenueCategory = revenueCategoryId?.let { revenueCategoryRepository.findByIds(userId, it) }

        return revenue.update(
            recipient = recipient,
            description = description,
            totalValue = totalValue,
            revenueCategory = revenueCategory?.first(),
            transactionDatetime = transactionDatetime
        )
    }

    @Transactional(TxType.REQUIRED)
    fun deleteMyRevenue(
        userId: UUID,
        revenueId: UUID,
    ) {
        val revenue = revenueRepository.findByExternalId(
            userId = userId,
            revenueId = revenueId
        ) ?: throw EntityNotFoundException("Revenue not found")
        revenueRepository.delete(revenue)
    }
}