package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.Rate
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.math.BigDecimal

class RateDeserializer() : StdDeserializer<Rate>(Rate::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Rate {
        val text = p.text
        return Rate(BigDecimal(text))
    }
}
