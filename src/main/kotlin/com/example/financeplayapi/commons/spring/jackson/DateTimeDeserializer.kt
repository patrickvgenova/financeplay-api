package com.example.financeplayapi.commons.spring.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.joda.time.DateTime

class DateTimeDeserializer() : StdDeserializer<DateTime>(DateTime::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): DateTime {
        val text = p.text
        return DateTime.parse(text)
    }
}
