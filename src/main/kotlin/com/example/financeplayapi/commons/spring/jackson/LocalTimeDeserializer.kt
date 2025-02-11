package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.util.DateTimeUtil
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.joda.time.LocalTime

class LocalTimeDeserializer() : StdDeserializer<LocalTime>(LocalTime::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalTime {
        val text = p.text
        if (text.length <= 5) {
            return DateTimeUtil.FORMAT_HM.parseLocalTime(text)
        } else {
            return DateTimeUtil.FORMAT_HMS.parseLocalTime(text)
        }
    }
}
