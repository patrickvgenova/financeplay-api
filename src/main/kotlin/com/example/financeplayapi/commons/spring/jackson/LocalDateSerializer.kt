package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.util.DateTimeUtil
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.joda.time.LocalDate

class LocalDateSerializer() : StdSerializer<LocalDate>(LocalDate::class.java) {

    override fun serialize(value: LocalDate?, gen: JsonGenerator, provider: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
        } else {
            gen.writeString(DateTimeUtil.FORMAT_YMD.print(value))
        }
    }
}
