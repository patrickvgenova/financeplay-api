package com.example.financeplayapi.commons.spring.domain

import com.example.financeplayapi.commons.core.util.trimToNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import kotlin.math.min

abstract class AbstractDomainService {

    protected fun getPage(
        page: Int? = null,
        offset: Int? = null,
        size: Int? = null,
        sort: String? = null,
        defSortedBy: Sort = Sort.unsorted(),
        sortOptions: List<String>? = null
    ): PageRequest {
        val finalSize = min(size ?: 1000, 1000)
        val finalPage = page ?: if (offset != null) offset * finalSize else 0
        val finalSort = parseSort(sort, defSortedBy, sortOptions?.map { it.lowercase() })
        return PageRequest.of(finalPage, finalSize, finalSort)
    }

    protected fun parseSort(
        str: String?,
        defaultValue: Sort = Sort.unsorted(),
        sortOptions: List<String>? = null
    ): Sort {
        if (str == null) return defaultValue
        val parts = str.split(",".toRegex())
        val ret = parts.map { it?.trimToNull() }.filter { it != null }.map { it!! }.map { p ->
            var attr = p
            var asc = Sort.Direction.ASC
            if (p.endsWith("_asc")) {
                attr = p.substring(0, p.length - 4)
            } else if (p.endsWith("_desc")) {
                attr = p.substring(0, p.length - 5)
                asc = Sort.Direction.DESC
            }
            if (sortOptions == null || sortOptions.contains(attr.lowercase())) {
                Order(asc, attr)
            } else {
                null
            }
        }
        val f = ret.filter { it != null }.map { it!! }
        if (f.isEmpty()) return Sort.unsorted()
        return Sort.by(f)
    }
}
