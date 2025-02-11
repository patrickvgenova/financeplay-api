package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.util.DateTimeUtil
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.joda.time.DateTime

class DateTimeSerializer() : StdSerializer<DateTime>(DateTime::class.java) {

    override fun serialize(value: DateTime?, gen: JsonGenerator, provider: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
        } else {
            gen.writeString(DateTimeUtil.ISO_8601_DATE_FORMAT.print(value))
        }
    }
}
