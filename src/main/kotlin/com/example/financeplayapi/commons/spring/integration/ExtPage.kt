package com.example.financeplayapi.commons.spring.integration

data class ExtPage<T>(
    val data: List<T>,
    val lastPage: Boolean,
    val totalPages: Int,
    val totalElements: Long,
    val page: Int,
) {
    companion object {
        private val EMPTY = ExtPage(emptyList<Any>(), true, 0, 0, 0)
        fun <T> empty(): ExtPage<T> {
            return EMPTY as ExtPage<T>
        }
    }
}
