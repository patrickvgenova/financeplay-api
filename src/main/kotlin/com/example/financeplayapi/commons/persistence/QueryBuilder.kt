package com.example.financeplayapi.commons.persistence

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Order
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.YearMonth
import java.util.*

/**
 * Contexto para construçao de consulta ao banco.
 *
 * @param R tipo retornado pela consulta.
 * @param T tipo da entidade raiz consultada
 */
class QueryBuilder<T, R>
/**
 * Construtor.
 *
 * @param cb    criteria builder
 * @param query query
 * @param ignoreSort flag paraignorar as restrições de ordenação. Usada para consultas do tipo count da paginacao
 */(private val cb: CriteriaBuilder, private val query: CriteriaQuery<R>, private val ignoreSort: Boolean = false) {

    private val predicates: MutableList<Predicate> = ArrayList()
    private val predicateTree = PredicateNodeGroup(NodePath(".1."), RestrictionType.AND)
    private val sort: MutableList<Order> = ArrayList()
    private var defaultGroup = CurrentGroup(NodePath(".1."), RestrictionType.AND)
    private var currentGroup = defaultGroup


    /**
     * Registra um critério de ordenação.
     *
     * @param attr atributo da ordenacao
     * @param ascending true para ordenação crescente; false caso contrário
     */
    /**
     * Registra um critério de ordenação.
     *
     * @param attr atributo da ordenacao
     */
    @JvmOverloads
    fun sort(attr: String, ascending: Boolean = true) {
        if (ignoreSort) return
        val e = getPath(attr)
        sort.add(OrderImpl(e, ascending))
        query.orderBy(sort)
    }

    fun group(path: String, type: RestrictionType): QueryBuilder<T, R> {
        this.currentGroup = CurrentGroup(NodePath(".1.$path."), type)
        return this
    }

    fun groupOr(path: String): QueryBuilder<T, R> {
        return group(path, RestrictionType.OR)
    }

    fun groupAnd(path: String): QueryBuilder<T, R> {
        return group(path, RestrictionType.AND)
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(predicate: Predicate): QueryBuilder<T, R> {
        addPredicate(predicate)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: String): QueryBuilder<T, R> {
        val p = getPredicateString(getPath(attribute) as Expression<String?>, op, value, false, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     * @param ignoreCase ignore case
     * @param not       negativa da operacao
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: String, ignoreCase: Boolean, not: Boolean): QueryBuilder<T, R> {
        val p = getPredicateString(getPath(attribute) as Expression<String?>, op, value, ignoreCase, not)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: UUID): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: Long): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: Double): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: Boolean): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: Enum<*>): QueryBuilder<T, R> {
        return where(attribute, op, value, false)
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     * @param not nega ou nao a operação de comparação
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: Enum<*>, not: Boolean): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, not)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: DateTime): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: LocalDate): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: YearMonth): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: LocalTime): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun where(attribute: String, op: Operation, value: Duration): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), op, value, false)
        addPredicate(p)
        return this
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun whereIsNull(attribute: String): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), Operation.IS_NULL, null, false)
        addPredicate(p)
        return this
    }

    fun whereStringIn(
        attribute: String,
        values: Collection<String>,
        ignoreCase: Boolean,
        not: Boolean,
    ): QueryBuilder<T, R> {
        val path: Expression<String?> = getPath(attribute) as Expression<String?>
        var resultPath: Expression<String?> = path
        var resultValue: Collection<String> = values
        if (ignoreCase) {
            resultPath = cb.lower(resultPath)
            resultValue = values.map { it.toLowerCase() }
        }

        var ret = cb.`in`(resultPath)
        for (title in resultValue) {
            ret.value(title)
        }
        if (not) {
            ret = ret.not() as CriteriaBuilder.In<String?>?
        }
        addPredicate(ret)
        return this
    }

    fun whereIn(attribute: String, values: Collection<Comparable<*>>, not: Boolean): QueryBuilder<T, R> {
        val path: Expression<Comparable<*>> = getPath(attribute)
        var resultPath: Expression<Comparable<*>> = path
        var resultValue: Collection<Comparable<*>> = values

        var ret = cb.`in`(resultPath)
        for (title in resultValue) {
            ret.value(title)
        }
        if (not) {
            ret = ret.not() as CriteriaBuilder.In<Comparable<*>>?
        }
        addPredicate(ret)
        return this
    }

    fun whereIn(attribute: String, values: Collection<Comparable<*>>): QueryBuilder<T, R> {
        return this.whereIn(attribute, values, false)
    }

    fun whereStringIn(attribute: String, values: Collection<String>): QueryBuilder<T, R> {
        return this.whereStringIn(attribute, values, false, false)
    }

    /**
     * Adiciona uma cláusula where à query.
     *
     * @param attribute atributo
     * @param op        operação de comparação
     * @param value     valor
     *
     * @return this
     */
    fun whereIsNotNull(attribute: String): QueryBuilder<T, R> {
        val p = getPredicateCommon(getPath(attribute), Operation.IS_NULL, null, true)
        addPredicate(p)
        return this
    }

    /**
     * Registra uma clausula where.
     *
     * @param p predicado where
     */
    private fun addPredicate(p: Predicate) {
        val g = predicateTree.getGroup(this.currentGroup.path)
        g.type = this.currentGroup.type
        g.predicates.add(PredicateNodeValue(p))
        query.where(predicateTree.toPredicate(cb))
        currentGroup = defaultGroup
    }

    /**
     * Traduz a string do atributo em expressão.
     *
     * @param attribute atributo
     *
     * @return expressão para o CriteriaApi
     */
    private fun getPath(attribute: String): Expression<Comparable<*>> {
        val root = query.roots.iterator().next() as Root<Comparable<*>>
        val split = attribute.split(".").toTypedArray()
        var ret: Path<Comparable<*>> = root
        for (attr in split) {
            ret = ret.get(attr)
        }
        return ret
    }

    /**
     * Gera o predicado de restrição para todos os tipos de atributo. Para os casos de atributos String, use {
     * [.getPredicateString]}.
     *
     * @param path  expressão do atributo da comparação
     * @param op    operação de comparação
     * @param value valor a ser comparado
     * @param not   negação do predicado
     *
     * @return predicado
     *
     * @see {{@link .getPredicateCommon
     */
    private fun getPredicateCommon(
        path: Expression<Comparable<*>>,
        op: Operation,
        value: Comparable<*>?,
        not: Boolean,
    ): Predicate {
        val p = path as Expression<Comparable<Any?>>

        var ret: Predicate = when (op) {
            Operation.EQ -> cb.equal(path, value)
            Operation.IS_NULL -> cb.isNull(path)
            Operation.GE -> cb.greaterThanOrEqualTo(p, value as Comparable<Any?>)
            Operation.GT -> cb.greaterThan(p, value as Comparable<Any?>)
            Operation.LE -> cb.lessThanOrEqualTo(p, value as Comparable<Any?>)
            Operation.LT -> cb.lessThan(p, value as Comparable<Any?>)
            Operation.NULL_FALSE -> cb.or(cb.equal(path, value), cb.isNull(path))
            else -> throw UnsupportedOperationException("Operacao nao implementada: $op com o valor $value")
        }
        if (not) {
            ret = ret.not()
        }
        return ret
    }

    /**
     * Gera o predicado de restrição para atributos String. Para outros tipo de atributo, usar
     * [.getPredicateCommon].
     *
     * @param path       caminho do atributo comparado
     * @param op         operacao de comparacao
     * @param value      valor comparado
     * @param ignoreCase ignora letras maiusculas e minusculas
     * @param not        negacao do atributo
     *
     * @return predicado
     *
     * @see .getPredicateCommon
     */
    private fun getPredicateString(
        path: Expression<String?>,
        op: Operation,
        value: String,
        ignoreCase: Boolean,
        not: Boolean,
    ): Predicate {
        var resultPath: Expression<String?> = path
        var resultValue: String = value
        if (ignoreCase) {
            resultPath = cb.lower(resultPath)
            resultValue = resultValue.lowercase(Locale.getDefault())
        }

        //TODO: verificar se há algum caso onde este cast não é seguro. Foi criado na migração deo kotlin 1.6 pra 1.9
        resultPath as Expression<String>

        var ret: Predicate = when (op) {
            Operation.CONTAINS -> cb.like(resultPath, "%$resultValue%")
            Operation.ENDS_WITH -> cb.like(resultPath, "%$resultValue")
            Operation.EQ -> cb.equal(resultPath, resultValue)
            Operation.IS_NULL -> cb.isNull(resultPath)
            Operation.GE -> cb.greaterThanOrEqualTo(resultPath, resultValue)
            Operation.GT -> cb.greaterThan(resultPath, resultValue)
            Operation.LE -> cb.lessThanOrEqualTo(resultPath, resultValue)
            Operation.LT -> cb.lessThan(resultPath, resultValue)
            Operation.STARTS_WITH -> cb.like(resultPath, "$resultValue%")
            Operation.NULL_FALSE -> cb.or(cb.equal(resultPath, java.lang.Boolean.FALSE), cb.isNull(resultPath))
            else -> throw UnsupportedOperationException("Operacao nao implementada: $op")
        }
        if (not) {
            ret = ret.not()
        }
        return ret
    }

    /**
     * Operação de comparação em cláusulas where.
     */
    enum class Operation {
        EQ, GT, GE, LT, LE, CONTAINS, STARTS_WITH, ENDS_WITH, NULL_FALSE, IS_NULL, IN
    }

    abstract class PredicateNode {
        abstract fun toPredicate(cb: CriteriaBuilder): Predicate
    }

    class PredicateNodeGroup(
        val groupPath: NodePath,
        var type: RestrictionType,
    ) : PredicateNode() {
        val predicates = mutableListOf<PredicateNode>()
        override fun toPredicate(cb: CriteriaBuilder): Predicate {
            if (type == RestrictionType.AND) {
                return cb.and(*this.predicates.map { it.toPredicate(cb) }.toTypedArray())
            } else {
                return cb.or(*this.predicates.map { it.toPredicate(cb) }.toTypedArray())
            }
        }

        fun getGroup(path: NodePath): PredicateNodeGroup {
            if (path.equals(this.groupPath)) return this
            if (path.isChildOrEqualOf(this.groupPath)) {
                val first: PredicateNodeGroup? =
                    this.predicates.filter { it is PredicateNodeGroup }.map { it as PredicateNodeGroup }
                        .firstOrNull { it.groupPath.isChildOrEqualOf(path) }

                if (first == null) {
                    val p = PredicateNodeGroup(path.subPath(this.groupPath.size() + 1), RestrictionType.AND)
                    this.predicates.add(p)
                    return p.getGroup(path)
                }
                return first.getGroup(path)
            } else {
                throw java.lang.IllegalStateException()
            }
        }
    }

    class PredicateNodeValue(
        private val predicate: Predicate,
    ) : PredicateNode() {
        override fun toPredicate(cb: CriteriaBuilder): Predicate {
            return this.predicate
        }
    }

    data class CurrentGroup(
        val path: NodePath,
        val type: RestrictionType,
    )

    enum class RestrictionType {
        AND, OR
    }
}

