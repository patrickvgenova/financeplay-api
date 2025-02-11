package com.example.financeplayapi.commons.persistence.converter

import jakarta.persistence.AttributeConverter
import org.joda.time.LocalTime
import java.sql.Time

class LocalTimeConverter : AttributeConverter<LocalTime?, Time?> {
    override fun convertToDatabaseColumn(attribute: LocalTime?): Time? {
        return localDateToDate(attribute)
    }

    override fun convertToEntityAttribute(dbData: Time?): LocalTime? {
        return dateToLocalDate(dbData)
    }

    companion object {
        fun localDateToDate(attribute: LocalTime?): Time? {
            return if (attribute == null) {
                null
            } else {
                Time(attribute.toDateTimeToday().millis)
            }
        }

        fun dateToLocalDate(dbData: Time?): LocalTime? {
            return if (dbData == null) {
                null
            } else {
                LocalTime(dbData.time)
            }
        }
    }
}
