package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.Money
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class MoneySerializer() : StdSerializer<Money>(Money::class.java) {

    override fun serialize(value: Money?, gen: JsonGenerator, provider: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
        } else {
            gen.writeNumber(value.toBigDecimal().toDouble())
        }
    }
}
