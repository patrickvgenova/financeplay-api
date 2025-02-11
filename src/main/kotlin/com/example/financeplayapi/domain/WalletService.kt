package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.exception.AccessDeniedException
import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.core.exception.PaymentRequiredException
import com.example.financeplayapi.commons.spring.domain.AbstractDomainService
import com.example.financeplayapi.domain.entity.Wallet
import com.example.financeplayapi.domain.repository.UserRepository
import com.example.financeplayapi.domain.repository.WalletRepository
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WalletService(
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository,
) : AbstractDomainService() {

    @Value("\${finance-play.wallet.limit.free}")
    private lateinit var limitFree: String

    @Transactional(TxType.REQUIRED)
    fun createWalletForUser(
        userId: UUID,
        name: String,
        color: String,
    ): Wallet {
        val user = userRepository.findByExternalId(userId) ?: throw EntityNotFoundException("User not found")

        if (user.plan == PlanType.FREE) {
            val wallets = walletRepository.find(userId)
            if (wallets.size >= limitFree.toInt()) {
                throw PaymentRequiredException("You have exceeded the creation limit allowed for your free account. To keep creating, upgrade to a paid account.")
            }
        }

        val wallet = Wallet(
            name = name,
            color = color,
            user = user
        )
        return walletRepository.add(wallet)
    }

    fun getUserWallets(
        userId: UUID,
        walletId: Long?,
        name: String?,
    ): List<Wallet> {
        if (walletId != null) {
            return walletRepository.findById(userId, walletId)?.let {
                listOf(it)
            } ?: throw EntityNotFoundException("Wallet not found")
        }
        return walletRepository.find(userId, name)
    }

    @Transactional(TxType.REQUIRED)
    fun changeUserWallet(
        userId: UUID,
        walletId: Long,
        name: String?,
        color: String?,
    ): Wallet {
        val wallet = walletRepository.findById(walletId) ?: throw EntityNotFoundException("Wallet not found")
        if (wallet.user.idExternal != userId) throw AccessDeniedException("This user does not have permission on the wallet")
        return wallet.updateFrom(name, color)
    }

    @Transactional(TxType.REQUIRED)
    fun deleteUserWallet(
        userId: UUID,
        id: Long,
    ) {
        val wallet = walletRepository.findById(id) ?: throw EntityNotFoundException("Wallet not found")
        if (wallet.user.idExternal != userId) throw AccessDeniedException("This user does not have permission on the wallet")
        return walletRepository.delete(wallet)
    }

}