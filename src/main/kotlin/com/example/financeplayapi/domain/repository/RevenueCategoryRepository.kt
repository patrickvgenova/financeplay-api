package com.example.financeplayapi.domain.repository

import com.example.financeplayapi.commons.persistence.AbstractRepository
import com.example.financeplayapi.commons.persistence.QueryBuilder
import com.example.financeplayapi.commons.persistence.QuerySpec
import com.example.financeplayapi.domain.entity.User
import com.example.financeplayapi.domain.entity.RevenueCategory
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositório para operações relacionadas à entidade [RevenueCategory].
 *
 * @property em EntityManager para acesso ao banco de dados.
 */
@Repository
class RevenueCategoryRepository(
    private val em: EntityManager
) : AbstractRepository<RevenueCategory>() {

    @Value("\${finance-play.revenue-category.default.name}")
    private lateinit var defaultRevenueCategoryValue: String

    @Value("\${finance-play.revenue-category.default.color}")
    private lateinit var defaultRevenueCategoryColor: String

    /**
     * Obtém a classe da entidade Audit.
     *
     * @return Classe da entidade Audit.
     */
    override fun getEntityClass(): Class<RevenueCategory> {
        return RevenueCategory::class.java
    }

    /**
     * Obtém o EntityManager associado ao repositório.
     *
     * @return EntityManager associado ao repositório.
     */
    override fun getEntityManager(): EntityManager {
        return this.em
    }

    fun findById(userId: UUID, id: Long): RevenueCategory? {
        return query(
            object : QuerySpec<RevenueCategory>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<RevenueCategory, *>) {
                    queryBuilder.where("${RevenueCategory.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    queryBuilder.where(RevenueCategory.ID__LONG, QueryBuilder.Operation.EQ, id)
                }
            }
        ).firstOrNull()
    }

    fun findByIds(userId: UUID, id: List<Long>): List<RevenueCategory> {
        return query(
            object : QuerySpec<RevenueCategory>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<RevenueCategory, *>) {
                    queryBuilder.where("${RevenueCategory.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    queryBuilder.whereIn(RevenueCategory.ID__LONG, id)
                }
            }
        )
    }

    @Transactional(TxType.REQUIRED)
    fun addDefaultRevenueCategoryForUser(user: User): RevenueCategory {
        return this.add(
            RevenueCategory(
                name = defaultRevenueCategoryValue,
                color = defaultRevenueCategoryColor,
                user = user,
            )
        )
    }


    fun find(
        userId: UUID,
        name: String? = null,
    ): List<RevenueCategory> {
        return query(
            object : QuerySpec<RevenueCategory>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<RevenueCategory, *>) {
                    queryBuilder.where("${RevenueCategory.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    if (!name.isNullOrEmpty()) {
                        queryBuilder.where(RevenueCategory.NAME__STRING, QueryBuilder.Operation.EQ, "$name")
                    }
                }
            }
        )
    }

}