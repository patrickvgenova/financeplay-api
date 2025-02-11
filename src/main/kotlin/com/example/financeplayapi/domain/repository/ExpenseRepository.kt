package com.example.financeplayapi.domain.repository

import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.commons.persistence.AbstractRepository
import com.example.financeplayapi.commons.persistence.QueryBuilder
import com.example.financeplayapi.commons.persistence.QuerySpec
import com.example.financeplayapi.domain.entity.*
import jakarta.persistence.EntityManager
import org.joda.time.DateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositório para operações relacionadas à entidade [Expense].
 *
 * @property em EntityManager para acesso ao banco de dados.
 */
@Repository
class ExpenseRepository(
    private val em: EntityManager
) : AbstractRepository<Expense>() {

    /**
     * Obtém a classe da entidade Audit.
     *
     * @return Classe da entidade Audit.
     */
    override fun getEntityClass(): Class<Expense> {
        return Expense::class.java
    }

    /**
     * Obtém o EntityManager associado ao repositório.
     *
     * @return EntityManager associado ao repositório.
     */
    override fun getEntityManager(): EntityManager {
        return this.em
    }

    fun findByExternalId(
        userId: UUID,
        expenseId: UUID,
    ): Expense? {
        return query(
            object : QuerySpec<Expense>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<Expense, *>) {
                    queryBuilder.where(
                        "${Expense.USER__USER}.${User.ID_EXTERNAL__UUID}",
                        QueryBuilder.Operation.EQ,
                        userId
                    )
                    queryBuilder.where(
                        Expense.ID_EXTERNAL__UUID,
                        QueryBuilder.Operation.EQ,
                        expenseId
                    )
                }
            }
        ).firstOrNull()
    }

    fun find(
        userId: UUID,
        recipient: String? = null,
        description: String? = null,
        quantity: Double? = null,
        unitValue: Money? = null,
        totalValue: Money? = null,
        expenseCategoryIds: List<Long>? = null,
        walletIds: List<Long>? = null,
        transactionDatetime: DateTime? = null,
        minTransactionDatetime: DateTime? = null,
        maxTransactionDatetime: DateTime? = null,
        pageable: Pageable
    ): Page<Expense> {
        return query(
            object : QuerySpec<Expense>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<Expense, *>) {
                    queryBuilder.where(
                            "${Expense.USER__USER}.${User.ID_EXTERNAL__UUID}",
                            QueryBuilder.Operation.EQ,
                            userId
                        )
                    recipient?.let {
                        queryBuilder.where(
                            Expense.RECIPIENT__STRING,
                            QueryBuilder.Operation.IN,
                            "$recipient"
                        )
                    }
                    description?.let {
                        queryBuilder.where(
                            Expense.DESCRIPTION__STRING,
                            QueryBuilder.Operation.IN,
                            "$description"
                        )
                    }
//                    quantity?.let {
//                        queryBuilder.where(
//                            Expense.QUANTITY__DOUBLE,
//                            QueryBuilder.Operation.EQ,
//                            quantity
//                        )
//                    }
//                    unitValue?.let {
//                        queryBuilder.where(
//                            Expense.UNIT_VALUE__MONEY,
//                            QueryBuilder.Operation.EQ,
//                            unitValue.toDouble()
//                        )
//                    }
//                    totalValue?.let {
//                        queryBuilder.where(
//                            Expense.TOTAL_VALUE__MONEY,
//                            QueryBuilder.Operation.EQ,
//                            totalValue.toDouble()
//                        )
//                    }
                    expenseCategoryIds?.let {
                        queryBuilder.whereIn(
                            "${Expense.CATEGORY__CATEGORY}.${ExpenseCategory.ID__LONG}",
                            expenseCategoryIds
                        )
                    }
//                    walletIds?.let {
//                        queryBuilder.whereIn(
//                            "${Expense.WALLETS__LIST_WALLET}.${Wallet.ID__LONG}",
//                            walletIds
//                        )
//                    }
                    if (transactionDatetime != null) {
                        queryBuilder.where(
                            Expense.TRANSACTION_DATETIME__DATETIME,
                            QueryBuilder.Operation.EQ,
                            transactionDatetime
                        )
                    } else if (minTransactionDatetime != null || maxTransactionDatetime != null) {
                        queryBuilder.groupAnd("1").where(
                            Expense.TRANSACTION_DATETIME__DATETIME,
                            QueryBuilder.Operation.GE,
                            minTransactionDatetime ?: DateTime.now()
                        ).groupAnd("1").where(
                            Expense.TRANSACTION_DATETIME__DATETIME,
                            QueryBuilder.Operation.LE,
                            maxTransactionDatetime ?: DateTime.now()
                        )
                    }
                }
            },
            pageable
        )
    }

}