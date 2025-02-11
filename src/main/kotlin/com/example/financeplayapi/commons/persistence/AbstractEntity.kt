package com.example.financeplayapi.commons.persistence

import org.slf4j.LoggerFactory
import java.util.*

abstract class AbstractEntity {
    override fun equals(o: Any?): Boolean {
        if (id == null) {
            return super.equals(o)
        }
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as AbstractEntity
        return id == that.id
    }

    /**
     * ID da entidade.
     *
     * @return ID
     */
    abstract val id: Long?

    override fun hashCode(): Int {
        return if (id == null) {
            super.hashCode()
        } else {
            Objects.hash(id)
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(javaClass.name)
    }
}
