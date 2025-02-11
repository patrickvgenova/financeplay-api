package com.example.financeplayapi.commons.persistence.converter

import jakarta.persistence.AttributeConverter
import org.joda.time.DateTime
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DateTimeConverter : AttributeConverter<DateTime?, Timestamp?> {
    override fun convertToDatabaseColumn(attribute: DateTime?): Timestamp? {
        return dateTimeToDate(attribute)
    }

    override fun convertToEntityAttribute(dbData: Timestamp?): DateTime? {
        return dateToDateTime(dbData)
    }

    companion object {

        private val tl: ThreadLocal<SimpleDateFormat> = ThreadLocal.withInitial {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        }

        fun dateTimeToDate(attribute: DateTime?): Timestamp? {
            return if (attribute == null) {
                null
            } else {
                Timestamp(attribute.millis)
            }
        }

        fun dateToDateTime(dbData: Date?): DateTime? {
            return if (dbData == null) {
                null
            } else {
                val str = this.tl.get().format(dbData)
                DateTime.parse(str)
            }
        }
    }
}
