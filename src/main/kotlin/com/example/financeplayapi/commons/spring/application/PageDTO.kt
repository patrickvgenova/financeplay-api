package com.example.financeplayapi.commons.spring.application

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort

data class PageDTO<T>(
    @field:Schema(description = "Lista de itens de página")
    val data: MutableList<T>,
    @field:Schema(description = "Indicação se a página atual é ou não a última página da coleção buscada")
    val lastPage: Boolean,
    @field:Schema(description = "Total de páginas da coleção buscada")
    val totalPages: Int,
    @field:Schema(description = "Total de elementos da coleção, quando não paginada")
    val totalElements: Long,
    @field:Schema(description = "Número da página atual")
    val page: Int,
    @field:Schema(description = "Dados de ordenação usada na busca")
    val sort: PageSort,
) {
    var query: MutableMap<String, Any?>? = null

    constructor(page: Page<T>) : this(
        data = page.content,
        lastPage = page.isLast,
        totalPages = page.totalPages,
        totalElements = page.totalElements,
        page = page.number,
        sort = PageSort(page),
    )

    data class PageSort(
        @field:Schema(description = "Indicação se a lista é ou não ordenada")
        val sorted: Boolean,
        @field:Schema(description = "Lista de atributos usado na ordenação da busca")
        val order: List<SortOrder>,
    ) {
        constructor(page: Page<*>) : this(
            sorted = page.sort.isSorted,
            order = page.pageable.sort.toList().map { SortOrder(it) },
        )
    }

    data class SortOrder(
        @field:Schema(description = "Atributo da ordenação")
        val property: String,
        @field:Schema(description = "Direção da ordenação (true para crescente, false para decrescente)")
        val asc: Boolean,
    ) {
        constructor(order: Sort.Order) : this(
            property = order.property,
            asc = order.isAscending,
        )
    }

    companion object {
        private val EMPTY = PageDTO<Unit>(
            data = mutableListOf(),
            lastPage = true,
            totalPages = 0,
            totalElements = 0,
            page = 0,
            sort = PageSort(
                sorted = false,
                order = listOf(),
            ),
        )

        fun <T> empty(): PageDTO<T> {
            return EMPTY as PageDTO<T>
        }
    }

    fun <Y> convert(converter: (T) -> Y): PageDTO<Y> {
        return PageDTO(
            data = data.map(converter).toMutableList(),
            lastPage = this.lastPage,
            totalPages = this.totalPages,
            totalElements = this.totalElements,
            page = this.page,
            sort = this.sort,
        )
    }

    fun extractQueryParams(request: HttpServletRequest?): PageDTO<T> {
        request?.parameterMap?.forEach { t, u ->
            val v = u.firstOrNull()
            addQuery(t, v)
        }
        return this
    }

    private fun addQuery(key: String, value: Any?) {
        if (this.query == null) {
            this.query = mutableMapOf()
        }
        this.query!![key] = value
    }
}
