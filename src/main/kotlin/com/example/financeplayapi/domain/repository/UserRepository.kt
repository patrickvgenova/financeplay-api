package com.example.financeplayapi.domain.repository

import com.example.financeplayapi.commons.persistence.AbstractRepository
import com.example.financeplayapi.commons.persistence.QueryBuilder
import com.example.financeplayapi.commons.persistence.QuerySpec
import com.example.financeplayapi.domain.entity.User
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repositório para operações relacionadas à entidade [User].
 *
 * @property em EntityManager para acesso ao banco de dados.
 */
@Repository
class UserRepository(
    private val em: EntityManager
) : AbstractRepository<User>() {

    /**
     * Obtém a classe da entidade Audit.
     *
     * @return Classe da entidade Audit.
     */
    override fun getEntityClass(): Class<User> {
        return User::class.java
    }

    /**
     * Obtém o EntityManager associado ao repositório.
     *
     * @return EntityManager associado ao repositório.
     */
    override fun getEntityManager(): EntityManager {
        return this.em
    }

    fun findByExternalId(userId: UUID): User? {
        return query(
            object : QuerySpec<User>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<User, *>) {
                    queryBuilder.where(User.ID_EXTERNAL__UUID, QueryBuilder.Operation.EQ, userId)
                }
            }
        ).firstOrNull()
    }

    fun findByEmail(email: String): User? {
        return query(
            object : QuerySpec<User>() {
                override fun applyPredicates(queryBuilder: QueryBuilder<User, *>) {
                    queryBuilder.where(User.EMAIL__STRING, QueryBuilder.Operation.EQ, email)
                }
            }
        ).firstOrNull()
    }

}