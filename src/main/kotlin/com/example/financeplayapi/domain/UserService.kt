package com.example.financeplayapi.domain

import com.example.financeplayapi.commons.core.exception.ConflictException
import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.spring.context.RequestContext
import com.example.financeplayapi.commons.spring.domain.AbstractDomainService
import com.example.financeplayapi.domain.entity.User
import com.example.financeplayapi.domain.repository.ExpenseCategoryRepository
import com.example.financeplayapi.domain.repository.RevenueCategoryRepository
import com.example.financeplayapi.domain.repository.UserRepository
import com.example.financeplayapi.domain.repository.WalletRepository
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val requestContext: RequestContext,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
    private val expenseCategoryRepository: ExpenseCategoryRepository,
    private val revenueCategoryRepository: RevenueCategoryRepository,
) : AbstractDomainService() {

    @Transactional(TxType.REQUIRED)
    fun createUser(
        name: String?,
        photoUrl: String?,
        document: String?,
        phoneNumber: String?,
        plan: PlanType,
    ): User {
        val email = requestContext.email!!
        val checkUser = userRepository.findByEmail(email)

        if (checkUser != null) throw ConflictException("There is already a user with this email")

        val user = userRepository.add(
            User(
                idExternal = requestContext.userId,
                name = name,
                email = email,
                photoUrl = photoUrl,
                document = document,
                phoneNumber = phoneNumber,
                plan = plan,
            )
        )

        createDefaultDataForUser(user.idExternal)

        return user
    }

    @Transactional(TxType.REQUIRED)
    fun findUser(userId: UUID): User {
        return userRepository.findByExternalId(userId)
            ?: createUser(
                null,
                null,
                null,
                null,
                PlanType.FREE
            )
    }

    @Transactional(TxType.REQUIRED)
    fun changeUser(
        userId: UUID,
        name: String?,
        photoUrl: String?,
        document: String?,
        phoneNumber: String?,
    ): User {
        val user = userRepository.findByExternalId(userId) ?: throw EntityNotFoundException("User not found")
        return user.updateFrom(
            name = name,
            photoUrl = photoUrl,
            document = document,
            phoneNumber = phoneNumber
        )
    }

    @Transactional(TxType.REQUIRED)
    fun deleteUser(userId: UUID) {
        val user = userRepository.findByExternalId(userId) ?: throw EntityNotFoundException("User not found")
        return userRepository.delete(user)
    }

    @Transactional(TxType.REQUIRED)
    fun subscribePlan(userId: UUID, plan: PlanType): User {
        val user = userRepository.findByExternalId(userId) ?: throw EntityNotFoundException("User not found")
        if (user.plan == plan) throw IllegalArgumentException("User is already subscribed to this plan")
        // TODO: Tratativas de pagamento e confirmação de pagamento
        return user.updateFrom(plan = plan)
    }

    @Transactional(TxType.REQUIRES_NEW)
    protected fun createDefaultDataForUser(userId: UUID) {
        val user = userRepository.findByExternalId(userId) ?: throw EntityNotFoundException("User not found")
        walletRepository.addDefaultWalletForUser(user)
        revenueCategoryRepository.addDefaultRevenueCategoryForUser(user)
        expenseCategoryRepository.addDefaultExpenseCategoryForUser(user)
        return
    }

}