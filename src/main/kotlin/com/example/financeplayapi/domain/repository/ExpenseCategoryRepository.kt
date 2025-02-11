package com.example.financeplayapi.domain.repository

import com.example.financeplayapi.commons.persistence.AbstractRepository
import com.example.financeplayapi.commons.persistence.QueryBuilder
import com.example.financeplayapi.commons.persistence.QuerySpec
import com.example.financeplayapi.domain.entity.User
import com.example.financeplayapi.domain.entity.ExpenseCategory
import com.example.financeplayapi.domain.entity.RevenueCategory
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositório para operações relacionadas à entidade [ExpenseCategory].
 *
 * @property em EntityManager para acesso ao banco de dados.
 */
@Repository
class ExpenseCategoryRepository(
    private val em: EntityManager
) : AbstractRepository<ExpenseCategory>() {
    
    @Value("\${finance-play.expense-category.default.name}")
    private lateinit var defaultExpenseCategoryValue: String

    @Value("\${finance-play.expense-category.default.color}")
    private lateinit var defaultExpenseCategoryColor: String

    /**
     * Obtém a classe da entidade Audit.
     *
     * @return Classe da entidade Audit.
     */
    override fun getEntityClass(): Class<ExpenseCategory> {
        return ExpenseCategory::class.java
    }

    /**
     * Obtém o EntityManager associado ao repositório.
     *
     * @return EntityManager associado ao repositório.
     */
    override fun getEntityManager(): EntityManager {
        return this.em
    }

    @Transactional(TxType.REQUIRED)
    fun addDefaultExpenseCategoryForUser(user: User): ExpenseCategory {
        return this.add(
            ExpenseCategory(
                name = defaultExpenseCategoryValue,
                color = defaultExpenseCategoryColor,
                user = user,
            )
        )
    }

    fun findById(userId: UUID, id: Long): ExpenseCategory? {
        return query(
            object : QuerySpec<ExpenseCategory>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<ExpenseCategory, *>) {
                    queryBuilder.where("${ExpenseCategory.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    queryBuilder.where(RevenueCategory.ID__LONG, QueryBuilder.Operation.EQ, id)
                }
            }
        ).firstOrNull()
    }

    fun findByIds(userId: UUID, id: List<Long>): List<ExpenseCategory> {
        return query(
            object : QuerySpec<ExpenseCategory>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<ExpenseCategory, *>) {
                    queryBuilder.where("${ExpenseCategory.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    queryBuilder.whereIn(RevenueCategory.ID__LONG, id)
                }
            }
        )
    }


    fun find(
        userId: UUID,
        name: String? = null,
    ): List<ExpenseCategory> {
        return query(
            object : QuerySpec<ExpenseCategory>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<ExpenseCategory, *>) {
                    queryBuilder.where("${ExpenseCategory.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    if (!name.isNullOrEmpty()) {
                        queryBuilder.where(ExpenseCategory.NAME__STRING, QueryBuilder.Operation.EQ, "$name")
                    }
                }
            }
        )
    }

}