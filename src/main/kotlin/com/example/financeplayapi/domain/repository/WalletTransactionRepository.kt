package com.example.financeplayapi.domain.repository

import com.example.financeplayapi.commons.persistence.AbstractRepository
import com.example.financeplayapi.commons.persistence.QueryBuilder
import com.example.financeplayapi.commons.persistence.QuerySpec
import com.example.financeplayapi.domain.entity.User
import com.example.financeplayapi.domain.entity.Wallet
import com.example.financeplayapi.domain.entity.WalletTransaction
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
class WalletTransactionRepository(
    private val em: EntityManager
) : AbstractRepository<WalletTransaction>() {

    /**
     * Obtém a classe da entidade Audit.
     *
     * @return Classe da entidade Audit.
     */
    override fun getEntityClass(): Class<WalletTransaction> {
        return WalletTransaction::class.java
    }

    /**
     * Obtém o EntityManager associado ao repositório.
     *
     * @return EntityManager associado ao repositório.
     */
    override fun getEntityManager(): EntityManager {
        return this.em
    }


    fun find(
        userId: UUID,
        transactionId: UUID? = null,
        walletId: Long? = null,
    ): List<WalletTransaction> {
        return query(
            object : QuerySpec<WalletTransaction>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<WalletTransaction, *>) {
                    queryBuilder.where("${WalletTransaction.WALLET__WALLET}.${Wallet.USER__USER}.${User.ID_EXTERNAL__UUID}", QueryBuilder.Operation.EQ, userId)

                    transactionId?.let {
                        queryBuilder.where(WalletTransaction.TRANSACTION__TRANSACTION, QueryBuilder.Operation.EQ, it)
                    }
                    walletId?.let {
                        queryBuilder.where(WalletTransaction.WALLET__WALLET, QueryBuilder.Operation.EQ, it)
                    }

                }
            }
        )
    }

}