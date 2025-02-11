package com.example.financeplayapi.commons.persistence.converter

import com.example.financeplayapi.commons.core.type.Money
import jakarta.persistence.AttributeConverter
import java.math.BigDecimal

class MoneyConverter : AttributeConverter<Money?, BigDecimal?> {
    override fun convertToDatabaseColumn(attribute: Money?): BigDecimal? {
        return attribute?.toBigDecimal()
    }

    override fun convertToEntityAttribute(dbData: BigDecimal?): Money? {
        return dbData?.let { Money(it) }
    }
}
