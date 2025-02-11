package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.util.DateTimeUtil
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.joda.time.LocalDate

class LocalDateDeserializer() : StdDeserializer<LocalDate>(LocalDate::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDate {
        val text = p.text
        return DateTimeUtil.FORMAT_YMD.parseLocalDate(text)
    }
}
