package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.CPF
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class CpfDeserializer() : StdDeserializer<CPF>(CPF::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): CPF {
        val text = p.text
        return CPF(text)
    }
}
