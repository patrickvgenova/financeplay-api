package com.example.financeplayapi.commons.persistence.converter

import com.example.financeplayapi.commons.core.type.Email
import jakarta.persistence.AttributeConverter

class EmailConverter : AttributeConverter<Email?, String?> {
    override fun convertToDatabaseColumn(attribute: Email?): String? {
        return attribute?.toEmailString()
    }

    override fun convertToEntityAttribute(dbData: String?): Email? {
        return dbData?.let { Email(it) }
    }
}
