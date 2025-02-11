package com.example.financeplayapi.commons.persistence

import jakarta.persistence.LockModeType


/**
 * Filtro de consultas de entidades.
 *
 * @param <T> tipo da entidade consultada
</T> */
@FunctionalInterface
abstract class QuerySpec<T> {
    /**
     * Gera a lista de predicados (clausula WHERE) da consulta.
     *
     * @param queryBuilder builder da consulta
     */
    abstract fun applyPredicates(queryBuilder: QueryBuilder<T, *>)

    open fun withLockMode(): LockModeType {
        return LockModeType.NONE
    }
}
