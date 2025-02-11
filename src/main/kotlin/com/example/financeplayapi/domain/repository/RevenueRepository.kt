package com.example.financeplayapi.domain.repository

import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.commons.persistence.AbstractRepository
import com.example.financeplayapi.commons.persistence.QueryBuilder
import com.example.financeplayapi.commons.persistence.QuerySpec
import com.example.financeplayapi.domain.entity.Revenue
import com.example.financeplayapi.domain.entity.RevenueCategory
import com.example.financeplayapi.domain.entity.User
import com.example.financeplayapi.domain.entity.Wallet
import jakarta.persistence.EntityManager
import org.joda.time.DateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositório para operações relacionadas à entidade [Revenue].
 *
 * @property em EntityManager para acesso ao banco de dados.
 */
@Repository
class RevenueRepository(
    private val em: EntityManager
) : AbstractRepository<Revenue>() {

    /**
     * Obtém a classe da entidade Audit.
     *
     * @return Classe da entidade Audit.
     */
    override fun getEntityClass(): Class<Revenue> {
        return Revenue::class.java
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
        revenueId: UUID,
    ): Revenue? {
        return query(
            object : QuerySpec<Revenue>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<Revenue, *>) {
                    queryBuilder.where(
                        "${Revenue.USER__USER}.${User.ID_EXTERNAL__UUID}",
                        QueryBuilder.Operation.EQ,
                        userId
                    )
                    queryBuilder.where(
                        Revenue.ID_EXTERNAL__UUID,
                        QueryBuilder.Operation.EQ,
                        revenueId
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
        revenueCategoryIds: List<Long>? = null,
        walletIds: List<Long>? = null,
        transactionDatetime: DateTime? = null,
        minTransactionDatetime: DateTime? = null,
        maxTransactionDatetime: DateTime? = null,
        pageable: Pageable
    ): Page<Revenue> {
        return query(
            object : QuerySpec<Revenue>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<Revenue, *>) {
                    userId.let {
                        queryBuilder.where(
                            "${Revenue.USER__USER}.${User.ID_EXTERNAL__UUID}",
                            QueryBuilder.Operation.EQ,
                            userId
                        )
                    }
                    recipient?.let {
                        queryBuilder.where(
                            Revenue.RECIPIENT__STRING,
                            QueryBuilder.Operation.IN,
                            "$recipient"
                        )
                    }
                    description?.let {
                        queryBuilder.where(
                            Revenue.DESCRIPTION__STRING,
                            QueryBuilder.Operation.IN,
                            "$description"
                        )
                    }
//                    quantity?.let {
//                        queryBuilder.where(
//                            Revenue.QUANTITY__DOUBLE,
//                            QueryBuilder.Operation.EQ,
//                            quantity
//                        )
//                    }
//                    unitValue?.let {
//                        queryBuilder.where(
//                            Revenue.UNIT_VALUE__MONEY,
//                            QueryBuilder.Operation.EQ,
//                            unitValue.toDouble()
//                        )
//                    }
//                    totalValue?.let {
//                        queryBuilder.where(
//                            Revenue.TOTAL_VALUE__MONEY,
//                            QueryBuilder.Operation.EQ,
//                            totalValue.toDouble()
//                        )
//                    }
                    revenueCategoryIds?.let {
                        queryBuilder.whereIn(
                            "${Revenue.CATEGORY__CATEGORY}.${RevenueCategory.ID__LONG}",
                            revenueCategoryIds
                        )
                    }
                    walletIds?.let {
                        queryBuilder.whereIn(
                            "${Revenue.WALLETS__LIST_WALLET}.${Wallet.ID__LONG}",
                            walletIds
                        )
                    }
                    if (transactionDatetime != null) {
                        queryBuilder.where(
                            Revenue.TRANSACTION_DATETIME__DATETIME,
                            QueryBuilder.Operation.EQ,
                            transactionDatetime
                        )
                    } else if (minTransactionDatetime != null || maxTransactionDatetime != null) {
                        queryBuilder.groupAnd("1").where(
                            Revenue.TRANSACTION_DATETIME__DATETIME,
                            QueryBuilder.Operation.GE,
                            minTransactionDatetime ?: DateTime.now()
                        ).groupAnd("1").where(
                            Revenue.TRANSACTION_DATETIME__DATETIME,
                            QueryBuilder.Operation.LT,
                            maxTransactionDatetime ?: DateTime.now()
                        )
                    }
                }
            },
            pageable
        )

    }

}