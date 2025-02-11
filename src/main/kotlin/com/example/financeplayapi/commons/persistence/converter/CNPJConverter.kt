package com.example.financeplayapi.commons.persistence.converter

import com.example.financeplayapi.commons.core.type.CNPJ
import jakarta.persistence.AttributeConverter

class CNPJConverter : AttributeConverter<CNPJ?, String?> {
    override fun convertToDatabaseColumn(attribute: CNPJ?): String? {
        return attribute?.printNumbers()
    }

    override fun convertToEntityAttribute(dbData: String?): CNPJ? {
        return dbData?.let { CNPJ(it) }
    }
}
