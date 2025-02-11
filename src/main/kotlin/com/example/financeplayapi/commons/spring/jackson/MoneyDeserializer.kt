package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.Money
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.math.BigDecimal

class MoneyDeserializer() : StdDeserializer<Money>(Money::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Money {
        val text = p.text
        return Money(BigDecimal(text))
    }
}
