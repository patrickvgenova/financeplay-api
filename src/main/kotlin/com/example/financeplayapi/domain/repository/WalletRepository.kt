package com.example.financeplayapi.domain.repository

import com.example.financeplayapi.commons.persistence.AbstractRepository
import com.example.financeplayapi.commons.persistence.QueryBuilder
import com.example.financeplayapi.commons.persistence.QuerySpec
import com.example.financeplayapi.domain.entity.User
import com.example.financeplayapi.domain.entity.Wallet
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.transaction.Transactional.TxType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repositório para operações relacionadas à entidade [Wallet].
 *
 * @property em EntityManager para acesso ao banco de dados.
 */
@Repository
class WalletRepository(
    private val em: EntityManager
) : AbstractRepository<Wallet>() {

    @Value("\${finance-play.wallet.default.name}")
    private lateinit var defaultWalletValue: String

    @Value("\${finance-play.wallet.default.color}")
    private lateinit var defaultWalletColor: String

    /**
     * Obtém a classe da entidade Audit.
     *
     * @return Classe da entidade Audit.
     */
    override fun getEntityClass(): Class<Wallet> {
        return Wallet::class.java
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
    fun addDefaultWalletForUser(user: User): Wallet {
        return this.add(
            Wallet(
                name = defaultWalletValue,
                color = defaultWalletColor,
                user = user,
            )
        )
    }

    fun findByIds(userId: UUID, ids: List<Long>): List<Wallet> {
        return query(
            object : QuerySpec<Wallet>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<Wallet, *>) {
                    queryBuilder.where("${Wallet.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    queryBuilder.whereIn(Wallet.ID__LONG, ids)
                }
            }
        )
    }

    fun findById(userId: UUID, id: Long): Wallet? {
        return query(
            object : QuerySpec<Wallet>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<Wallet, *>) {
                    queryBuilder.where("${Wallet.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    queryBuilder.where(Wallet.ID__LONG, QueryBuilder.Operation.EQ, id)
                }
            }
        ).firstOrNull()
    }

    fun find(
        userId: UUID,
        name: String? = null,
    ): List<Wallet> {
        return query(
            object : QuerySpec<Wallet>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<Wallet, *>) {
                    queryBuilder.where("${Wallet.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    if (!name.isNullOrEmpty()) {
                        queryBuilder.where(Wallet.NAME__STRING, QueryBuilder.Operation.CONTAINS, "$name")
                    }
                }
            }
        )
    }

}