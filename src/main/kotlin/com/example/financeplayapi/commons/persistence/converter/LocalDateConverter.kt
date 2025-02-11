package com.example.financeplayapi.commons.persistence.converter

import com.example.financeplayapi.commons.core.util.DateTimeUtil
import jakarta.persistence.AttributeConverter
import org.joda.time.LocalDate
import java.sql.Date
import java.text.SimpleDateFormat

class LocalDateConverter : AttributeConverter<LocalDate?, Date?> {
    override fun convertToDatabaseColumn(attribute: LocalDate?): Date? {
        return localDateToDate(attribute)
    }

    override fun convertToEntityAttribute(dbData: Date?): LocalDate? {
        return dateToLocalDate(dbData)
    }

    companion object {
        private val tl: ThreadLocal<SimpleDateFormat> = ThreadLocal.withInitial {
            SimpleDateFormat("yyyy-MM-dd")
        }

        fun localDateToDate(attribute: LocalDate?): Date? {
            return if (attribute == null) {
                null
            } else {
                Date(attribute.toDate().time)
            }
        }

        fun dateToLocalDate(dbData: Date?): LocalDate? {
            return if (dbData == null) {
                null
            } else {
                val message = tl.get().format(dbData)
                DateTimeUtil.FORMAT_YMD.parseLocalDate(message)
            }
        }
    }
}
