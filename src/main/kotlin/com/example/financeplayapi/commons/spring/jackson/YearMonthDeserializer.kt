package com.example.financeplayapi.commons.spring.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.joda.time.YearMonth

class YearMonthDeserializer() : StdDeserializer<YearMonth>(YearMonth::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): YearMonth {
        val text = p.text
        return YearMonth(text)
    }
}
