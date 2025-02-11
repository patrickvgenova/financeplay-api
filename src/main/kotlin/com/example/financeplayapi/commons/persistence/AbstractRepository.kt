package com.example.financeplayapi.commons.persistence

import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import kotlin.math.min

abstract class AbstractRepository<T : AbstractEntity> {

    protected abstract fun getEntityManager(): EntityManager
    protected abstract fun getEntityClass(): Class<T>

    fun list(): Page<T> {
        val data = getEntityManager().createQuery("SELECT e FROM ${this.getEntityClass().name} e").resultList as List<T>
        return PageImpl(data, Pageable.unpaged(), data.size.toLong())
    }

    fun add(t: T): T {
        getEntityManager().persist(t)
        getEntityManager().flush()
        return t
    }

    fun delete(ent: T) {
        this.getEntityManager().remove(ent)
        this.getEntityManager().flush()
    }

    fun findById(id: Long): T? {
        return this.getEntityManager().find(this.getEntityClass(), id)
    }

    /**
     * Consulta customizada.
     *
     * @param spec especificacoes da consulta
     *
     * @return lista resultante da consulta
     */
    fun query(spec: QuerySpec<T>): List<T> {
        return innerQuery(spec, getPage(null, null, Sort.unsorted())).content
    }

    /**
     * Executa uma consulta com ordenacao.
     *
     * @param spec critários de filtragem
     * @param sort ordenação dos resultados
     *
     * @return lista resultante da consulta
     */
    fun query(spec: QuerySpec<T>, sort: Sort): List<T> {
        return innerQuery(spec, getPage(null, null, sort)).content
    }

    /**
     * Executa uma consulta paginada.
     *
     * @param spec critérios de filtragem
     * @param page dados da paginação e ordenação
     *
     * @return página de resultados
     */
    fun query(spec: QuerySpec<T>, page: Pageable): Page<T> {
        return innerQuery(spec, page)
    }

    /**
     * Cria uma paginacao a partir dos atributos de offset e maxresults.
     *
     * @param offset     offset
     * @param maxResults maxresults
     * @param sort       ordenação do resultado
     *
     * @return pagina
     */
    private fun getPage(offset: Int?, maxResults: Int?, sort: Sort): Pageable {
        return PageRequest.of(offset ?: 0, min(maxResults ?: 500, 500), sort)
    }

    /**
     * Faz uma consulta do total de itens, independente da offset e maxresults.
     *
     * @param spec criterios de filtragem
     *
     * @return contagem de itens que atendem os criterios de filtragem
     */
    fun getTotal(spec: QuerySpec<T>): Long {
        val tClass: Class<T> = this.getEntityClass()
        val cb = getEntityManager().criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        query.select(cb.count(query.from(tClass)))
        val qb = QueryBuilder<T, Long>(cb, query)
        spec.applyPredicates(qb)
        val typedQuery = getEntityManager().createQuery(query)
        return typedQuery.singleResult
    }

    /**
     * Metodo comum para execução propriamente dita da query.
     *
     * @param spec critérios de filtragem
     * @param page dados da paginação e ordenação
     *
     * @return página de resultados
     */
    private fun innerQuery(spec: QuerySpec<T>, page: Pageable): Page<T> {
        val data: List<T> = getData(spec, page)
        val total: Long
        if (page.isPaged) {
            if (page.offset == 0L && page.pageSize > data.size) {
                total = data.size.toLong()
            } else {
                total = getTotal(spec)
            }
        } else {
            total = data.size.toLong()
        }
        return PageImpl(data, page, total)
    }

    /**
     * Executa a query para obter a lista com os dados.
     *
     * @param spec criterios de restricao da query
     * @param page paginacao
     *
     * @return dados selecionados
     */
    private fun getData(spec: QuerySpec<T>, page: Pageable): List<T> {
        val tClass: Class<T> = this.getEntityClass()
        val cb = getEntityManager().criteriaBuilder
        val query = cb.createQuery(tClass)
        val root = query.from(tClass)
        val qb: QueryBuilder<T, T> = QueryBuilder(cb, query)
        spec.applyPredicates(qb)
        for (order in page.sort) {
            qb.sort(order.property, order.isAscending)
        }
        val typedQuery = getEntityManager().createQuery(query)
        val lock = spec.withLockMode()
        typedQuery.setLockMode(lock)
        if (page.isPaged) {
            typedQuery.firstResult = page.offset.toInt()
            typedQuery.maxResults = page.pageSize
        }
        return typedQuery.resultList
    }

    fun findByIdWithLock(id: Long): T? {
        return this.getEntityManager().find(this.getEntityClass(), id, LockModeType.PESSIMISTIC_WRITE)
    }
}
