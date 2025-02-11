package com.example.financeplayapi.commons.persistence.converter

import com.example.financeplayapi.commons.core.type.CPF
import jakarta.persistence.AttributeConverter

class CPFConverter : AttributeConverter<CPF?, String?> {
    override fun convertToDatabaseColumn(attribute: CPF?): String? {
        return attribute?.printNumbers()
    }

    override fun convertToEntityAttribute(dbData: String?): CPF? {
        return dbData?.let { CPF(it) }
    }
}
