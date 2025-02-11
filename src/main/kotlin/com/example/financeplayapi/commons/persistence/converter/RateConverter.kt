package com.example.financeplayapi.commons.persistence.converter


import com.example.financeplayapi.commons.core.type.Rate
import jakarta.persistence.AttributeConverter
import java.math.BigDecimal

class RateConverter : AttributeConverter<Rate?, BigDecimal?> {
    override fun convertToDatabaseColumn(attribute: Rate?): BigDecimal? {
        return attribute?.toBigDecimal()
    }

    override fun convertToEntityAttribute(dbData: BigDecimal?): Rate? {
        return dbData?.let { Rate(it) }
    }
}
