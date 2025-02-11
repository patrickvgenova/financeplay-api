package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.Rate
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class RateSerializer() : StdSerializer<Rate>(Rate::class.java) {

    override fun serialize(value: Rate?, gen: JsonGenerator, provider: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
        } else {
            gen.writeNumber(value.toBigDecimal().toDouble())
        }
    }
}
