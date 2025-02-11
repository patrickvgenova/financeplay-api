package com.example.financeplayapi.commons.persistence.converter

import jakarta.persistence.AttributeConverter
import org.joda.time.LocalDate
import org.joda.time.YearMonth
import java.sql.Date

class YearMonthConverter : AttributeConverter<YearMonth?, Date?> {
    override fun convertToDatabaseColumn(attribute: YearMonth?): Date? {
        return localDateToDate(attribute)
    }

    override fun convertToEntityAttribute(dbData: Date?): YearMonth? {
        return dateToLocalDate(dbData)
    }

    companion object {
        fun localDateToDate(attribute: YearMonth?): Date? {
            return if (attribute == null) {
                null
            } else {
                Date(attribute.toLocalDate(1).toDate().time)
            }
        }

        fun dateToLocalDate(dbData: Date?): YearMonth? {
            return if (dbData == null) {
                null
            } else {
                YearMonth(LocalDate(dbData.time))
            }
        }
    }
}
